package core.rule;

import core.board.BoardViewer;
import core.board.Tile;
import core.move.Move;
import core.piece.LatentAttackPiece;
import core.piece.OptimizedPiece;
import core.piece.Piece;
import core.piece.PieceClass;
import core.piece.PieceRule;
import core.piece.SpecialMovePiece;
import core.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

/**
 * Represents a set of rules associated with an instance of game
 */
public final class Rules<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final RuleBindings<C, P, B> ruleBindings;

    public Rules(RuleBindings<C, P, B> ruleBindings) {
        this.ruleBindings = ruleBindings;
    }

    private void throwUnlessPieceExistsAndBelongsToPlayer(B board, C position, Player player)
            throws IllegalStateException {

        if (!board.getPiece(position).isPresent()) {
            throw new IllegalStateException(player + " try to move non-existing piece at " + position);
        }
        if (!board.getPiece(position).get().getPlayer().equals(player)) {
            throw new IllegalStateException(player + " does not own piece at " + position);
        }
    }

    public Collection<LatentAttack<C>> latentAttacking(B board, C source, Player pinner) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, pinner);
        Piece<P> piece = board.getPiece(source).get();
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());

        if (!(rule instanceof OptimizedPiece)) {
            throw new IllegalStateException("This game contains pieces that is not optimized: " + piece.getPieceClass());
        }
        if (!(rule instanceof LatentAttackPiece)) {
            return ImmutableList.of();
        }
        LatentAttackPiece<C, P, B> latentAttack = (LatentAttackPiece<C, P, B>) rule;
        return latentAttack.latentAttacking(board, source, pinner);
    }

    public Collection<C> attacking(B board, C source, Player attacker) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, attacker);
        Piece<P> piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass()).attacking(board, source, attacker);
    }

    public Collection<Move<C, P>> specialMove(B board, C source, Player actor) {
        throwUnlessPieceExistsAndBelongsToPlayer(board, source, actor);
        Piece<P> piece = board.getPiece(source).get();
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());
        if (rule instanceof SpecialMovePiece) {
            SpecialMovePiece<C, P, B> specialRule = (SpecialMovePiece<C, P, B>) rule;
            return specialRule.specialMove(board, actor);
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
        PieceRule<C, P, B> rule = ruleBindings.getRule(piece.getPieceClass());
        if (!(rule instanceof OptimizedPiece)) {
            throw new IllegalStateException("This game contains pieces that is not optimized: " + piece.getPieceClass());
        }
        OptimizedPiece<C, P, B> optimizedRule = (OptimizedPiece<C, P, B>) rule;
        return optimizedRule.attackBlockingPositions(board, source, target, attacker);
    }
}
