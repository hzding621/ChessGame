package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.game.DefenderInformation;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a set of rules associated with an instance of game
 */
public class Rules<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final RuleBindings<C, P, B> ruleBindings;

    public Rules(RuleBindings<C, P, B> ruleBindings) {
        this.ruleBindings = ruleBindings;
    }

    private boolean canMoveByPlayer(B board, C position, Player player) {
        if (!board.getPiece(position).isPresent()) {
            return false;
        }
        Piece piece = board.getPiece(position).get();
        return  piece.getPlayer().equals(player);
    }

    public Collection<Pin<C>> pinning(B board, C source, Player pinner) {
        if (!board.getPiece(source).isPresent()) {
            throw new IllegalStateException("Get pinning set of non-existing piece at " + source);
        }
        if (!canMoveByPlayer(board, source, pinner)) {
            throw new IllegalStateException("Player " + pinner + " cannot move " + source);
        }
        Piece<P> piece = board.getPiece(source).get();
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());
        if (!(rule instanceof PinningPieceRule)) {
            return Collections.emptyList();
        }
        PinningPieceRule<C, P, B> pinningRule = (PinningPieceRule<C, P, B>) rule;
        return pinningRule.pinningAttack(source, pinner);
    }

    public Collection<C> attacking(B board, C source, Player actor) {
        if (!board.getPiece(source).isPresent()) {
            throw new IllegalStateException("Get attacking set of non-existing piece at " + source);
        }
        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass())
                .attacking(source, actor);
    }

    public Collection<C> basicMoves(B board, C source, Player actor) {
        if (!canMoveByPlayer(board, source, actor)) {
            throw new IllegalStateException("Player " + actor + " cannot move " + source);
        }

        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass())
                .basicMoves(source, actor);
    }

    public boolean canRemoveCheckThreats(C movedToPosition, Player checker, C actorKing,
                                         DefenderInformation<C, P, B> information) {

        int remainingCheckers = information.getCheckers().size();
        for (PieceLocator<C, P> checkerLocator: information.getCheckers()) {
            if (ruleBindings.getRule(checkerLocator.getPiece().getPieceClass())
                    .getBlockingPositionsWhenAttacking(checkerLocator.getCell(),
                            actorKing, checker)
                    .stream()
                    .anyMatch(c -> c.equals(movedToPosition))) {
                remainingCheckers--;
            }
        }
        return remainingCheckers == 0;
    }
}
