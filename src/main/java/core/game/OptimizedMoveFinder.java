package core.game;

import core.board.BoardViewer;
import core.board.Tile;
import core.move.AnnotatedSimpleMove;
import core.move.Move;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;
import core.rule.Attack;
import core.rule.LatentAttack;
import core.rule.Rules;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Optimized move finder by utilizing mainly latent attacks information. Not applicable for non-standard chess pieces
 */
public final class OptimizedMoveFinder<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>>
        implements MoveFinder<C, P> {

    private final SetMultimap<C, Move<C, P>> availableMoves = MultimapBuilder.treeKeys().hashSetValues().build();
    private final SetMultimap<C, LatentAttack<C>> latentCheckersByBlocker = MultimapBuilder.treeKeys().hashSetValues().build();

    private final B board;
    private final Rules<C, P, B> rules;
    private final RuntimeInformation<C, P> runtimeInformation;

    public OptimizedMoveFinder(B board, Rules<C, P, B> rules, RuntimeInformation<C, P> runtimeInformation) {
        this.board = board;
        this.rules = rules;
        this.runtimeInformation = runtimeInformation;
    }

    @Override
    public void recompute() {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();
        availableMoves.clear();
        latentCheckersByBlocker.clear();

        // Iterate through all the pieces of the current defenders
        board.getPieceLocationsOfPlayer(defender).forEach(defenderPosition -> {

            // Get the positions a defending piece are latently attacking
            rules.latentAttacking(board, defenderPosition, defender).forEach(latentAttack -> {
                if (latentAttack.getAttacked().equals(runtimeInformation.getPieceInformation().locateKing(actor))) {
                    // If the hided piece is same as actor's king, register the protecting piece in the kingDefenders
                    latentCheckersByBlocker.put(latentAttack.getBlocker(), latentAttack);
                }
            });
        });

        board.getPieceLocationsOfPlayer(actor).forEach(sourcePosition -> availableMoves.putAll(sourcePosition,
                computeAvailableMoves(sourcePosition)));
    }

    /**
     * This optimized version computes all actual available moves for a player with its piece at sourcePosition
     * by using attackInformation. This method finds moves that only deactivate check threats and only does not put
     * king into new check
     * @return the collection of available moves
     */
    private Collection<Move<C, P>> computeAvailableMoves(C sourcePosition) {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();
        Piece<P> sourcePiece = board.getPiece(sourcePosition).get();

        // Get checkers, and compute its blocking positions
        // At this point all pieces involved will be checked if they implemented the OptimizedPiece interface
        // If not then this game should not have used OptimizedMoveFinder, and the rules dispatcher will throw
        Collection<Attack<C>> checkers = runtimeInformation.getAttackInformation().getCheckers().stream()
                .map(checker -> new Attack<>(checker, rules.attackBlockingPositions(board, checker,
                        runtimeInformation.getPieceInformation().locateKing(actor), defender)))
                .collect(Collectors.toList());

        // Get latent checkers contingent on the actor piece moving
        Collection<LatentAttack<C>> latentCheckers = latentCheckersByBlocker.get(sourcePosition);

        // Get all potential moves for the actor piece
        Collection<Move<C, P>> moves = rules.basicMoves(board, sourcePosition, actor)
                .stream()
                .filter(targetPosition ->
                        board.getPiece(targetPosition).map(p -> p.getPieceClass().canCapture()).orElse(true))
                .filter(targetPosition ->

                        /*
                         * Either the piece is King and it is moving to a non-attacked position (can always do that)
                         * Or the move removes all poses check (but might expose the King, will be filtered below)
                         */
                        sourcePiece.getPieceClass().isKing()
                                ? !runtimeInformation.getAttackInformation().isAttacked(targetPosition)
                                : checkers.stream().allMatch(attack -> attack.getBlockingPositions().contains(targetPosition)))


                .filter(targetPosition ->

                        /*
                         * If an actor piece is a king defender, this move must maintain the pin
                         * Filter condition translation:
                         *      For all pins, where the blocker is the source position
                         *          the target position must maintain the pin (must not expose the king)
                         */
                        latentCheckers.stream()
                                .allMatch(pin -> pin.getMaintainingPositions().contains(targetPosition)))

                .map(targetPosition -> AnnotatedSimpleMove.of(sourcePiece, sourcePosition, targetPosition, actor))
                .collect(Collectors.toList());

        // Add special moves as well. Checking validity is up to the special move implementation
        moves.addAll(rules.specialMove(board, sourcePosition, actor));

        return moves;
    }

    @Override
    public SetMultimap<C, Move<C, P>> getAvailableMoves() {
        return availableMoves;
    }
}
