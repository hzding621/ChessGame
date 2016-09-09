package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.game.DefenderInformation;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Represents a set of rules associated with an instance of game
 */
public class Rules<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final RuleBindings<C, P, B> ruleBindings;

    public Rules(RuleBindings<C, P, B> ruleBindings) {
        this.ruleBindings = ruleBindings;
    }

    private void throwUnlessPieceExistsAndBelongsToPlayer(B board, C position, Player player)
            throws IllegalStateException {

        if (!board.getPiece(position).isPresent()) {
            throw new IllegalStateException(player + " try to move non-existing pice at " + position);
        }
        if (!board.getPiece(position).get().getPlayer().equals(player)) {
            throw new IllegalStateException(player + " does not own piece at " + position);
        }
    }

    public Collection<LatentAttack<C>> latentAttacking(B board, C source, Player pinner) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, pinner);
        Piece<P> piece = board.getPiece(source).get();
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());
        if (!(rule instanceof LatentAttackPiece)) {
            return Collections.emptyList();
        }
        LatentAttackPiece<C, P, B> latentAttack = (LatentAttackPiece<C, P, B>) rule;
        return latentAttack.latentAttacking(source, pinner);
    }

    public Collection<C> attacking(B board, C source, Player attacker) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, attacker);
        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).attacking(source, attacker);
    }

    public Collection<C> basicMoves(B board, C source, Player actor) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, actor);

        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).basicMoves(source, actor);
    }

    public Collection<C> attackBlockingPositions(B board, C source, C target, Player attacker) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, attacker);

        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).attackBlockingPositions(source, target, attacker);
    }

    /**
     * The main method to compute all actual available moves for a player with its piece at sourcePosition
     * This method finds moves that only deactivate check threats and only does not put king into new check
     * @return the collection of available moves
     */
    public Collection<Move<C>> computeAvailableMovesOptimized(B board,
                                                              C sourcePosition,
                                                              Player actor,
                                                              DefenderInformation<C, P, B> defenderInformation) {
        // Get checkers
        Collection<Attack<C>> checkers = defenderInformation.getCheckers();

        // Get latent checkers contingent on the actor piece moving
        Collection<LatentAttack<C>> latentCheckers = defenderInformation.getLatentCheckersByBlocker(sourcePosition);

        // Get all potential moves for the actor piece
        return basicMoves(board, sourcePosition, actor)
                .stream()
                .filter(targetPosition ->

                        /*
                         * Either the piece is King and it is moving to a non-attacked position (can always do that)
                         * Or the move removes all poses check (but might expose the King, will be filtered below)
                         */
                        board.getPiece(sourcePosition).get().getPieceClass().isKing()
                                ? !defenderInformation.isAttacked(targetPosition)
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
}
