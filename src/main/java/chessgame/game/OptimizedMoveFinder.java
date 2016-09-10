package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;
import chessgame.rule.Rules;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Default implementation of Actor Information
 */
public class OptimizedMoveFinder<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements MoveFinder<C, P> {
    private final SetMultimap<C, Move<C>> availableMoves = MultimapBuilder.treeKeys().hashSetValues().build();
    private final SetMultimap<C, LatentAttack<C>> latentCheckersByBlocker = MultimapBuilder.treeKeys().hashSetValues().build();

    private final B board;
    private final Rules<C, P, B> rules;
    private final RuntimeInformation<C, P> runtimeInformation;

    public OptimizedMoveFinder(B board, Rules<C, P, B> rules, RuntimeInformation<C, P> runtimeInformation) {
        this.board = board;
        this.rules = rules;
        this.runtimeInformation = runtimeInformation;
    }

    /**
     * This method recompute the entire actor information, which includes all available moves for the actor.
     * This also allows to check whether the actor has been checkmated, i.e. there are no valid moves for the actor
     * This method runs after every move is made and the defender information has been updated.
     */
    @Override
    public void recompute() {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();
        availableMoves.clear();
        latentCheckersByBlocker.clear();

        // Iterate through all the pieces of the current defenders
        board.getPiecesForPlayer(defender).forEach(defenderPosition -> {

            // Get the positions a defending piece are latently attacking
            rules.latentAttacking(board, defenderPosition, defender).forEach(latentAttack -> {
                if (latentAttack.getAttacked().equals(runtimeInformation.getPieceInformation().locateKing(actor))) {
                    // If the hided piece is same as actor's king, register the protecting piece in the kingDefenders
                    latentCheckersByBlocker.put(latentAttack.getBlocker(), latentAttack);
                }
            });
        });

        board.getPiecesForPlayer(actor).forEach(sourcePosition -> availableMoves.putAll(sourcePosition,
                computeAvailableMoves(sourcePosition)));
    }

    /**
     * This optimized version computes all actual available moves for a player with its piece at sourcePosition
     * by using attackInformation. This method finds moves that only deactivate check threats and only does not put
     * king into new check
     * @return the collection of available moves
     */
    private Collection<Move<C>> computeAvailableMoves(C sourcePosition) {

        Player actor = runtimeInformation.getPlayerInformation().getActor();

        // Get checkers
        Collection<Attack<C>> checkers = runtimeInformation.getAttackInformation().getCheckers();

        // Get latent checkers contingent on the actor piece moving
        Collection<LatentAttack<C>> latentCheckers = latentCheckersByBlocker.get(sourcePosition);

        // Get all potential moves for the actor piece
        return rules.basicMoves(board, sourcePosition, actor)
                .stream()
                .filter(targetPosition ->

                        /*
                         * Either the piece is King and it is moving to a non-attacked position (can always do that)
                         * Or the move removes all poses check (but might expose the King, will be filtered below)
                         */
                        board.getPiece(sourcePosition).get().getPieceClass().isKing()
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

                .map(targetPosition -> SimpleMove.of(sourcePosition, targetPosition, actor))
                .collect(Collectors.toList());
    }

    @Override
    public SetMultimap<C, Move<C>> getAvailableMoves() {
        return availableMoves;
    }
}