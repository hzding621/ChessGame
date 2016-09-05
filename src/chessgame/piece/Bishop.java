package chessgame.piece;

import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.board.Cell;
import chessgame.player.Player;
import chessgame.rule.DirectionalAttackingPieceRule;
import chessgame.rule.PieceRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that implements Bishop piece moving logic
 */
public final class Bishop<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Bishop(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Bishop{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class BishopRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridBoard<C, D, A, P>> implements DirectionalAttackingPieceRule<C, D, A, P, B> {
        @Override
        public Collection<D> getAttackingDirections(B board) {
            return board.getDiagonalDirections();
        }
    }
}