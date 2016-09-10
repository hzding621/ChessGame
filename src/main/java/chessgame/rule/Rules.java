package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

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
        return latentAttack.latentAttacking(board, source, pinner);
    }

    public Collection<C> attacking(B board, C source, Player attacker) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, attacker);
        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).attacking(board, source, attacker);
    }

    @SuppressWarnings(value = "unchecked")
    public Collection<Move<C>> specialMove(B board, C source, Player actor) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, actor);
        Piece<P> piece = board.getPiece(source).get();
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());
        if (rule instanceof SpecialMovePieceRule) {
            return ((SpecialMovePieceRule) rule).specialMove(board, actor);
        } else {
            return ImmutableList.of();
        }
    }

    public Collection<C> basicMoves(B board, C source, Player actor) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, actor);

        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).basicMoves(board, source, actor);
    }

    public Collection<C> attackBlockingPositions(B board, C source, C target, Player attacker) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, attacker);

        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).attackBlockingPositions(board, source, target, attacker);
    }
}
