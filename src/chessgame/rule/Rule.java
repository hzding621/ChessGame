package chessgame.rule;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by haozhending on 9/4/16.
 */
public class Rule<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    private final RuleBindings<C, A, P, B> ruleBindings;

    public Rule(RuleBindings<C, A, P, B> ruleBindings) {
        this.ruleBindings = ruleBindings;
    }

    public boolean canMoveTo(B board, C source, C target, Player player) {
        if (!canMoveByPlayer(board, source, player)) {
            return false;
        }
        P piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass())
                        .getNormalMoves(board, source, player)
                        .stream().anyMatch(c -> c.equals(target));
    }

    private boolean canMoveByPlayer(B board, C position, Player player) {
        if (!board.getPiece(position).isPresent()) {
            return false;
        }
        P piece = board.getPiece(position).get();
        return  piece.getPlayer().equals(player);
    }

    public Collection<C> getPossibleMoves(B board, C source, Player player) {
        if (!canMoveByPlayer(board, source, player)) {
            return Collections.EMPTY_LIST;
        }

        P piece = board.getPiece(source).get();
        return ruleBindings.getRule(piece.getPieceClass())
                .getNormalMoves(board, source, player);
    }
}
