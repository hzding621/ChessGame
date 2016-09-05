package chessgame.piece;

import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.board.Cell;
import chessgame.player.Player;
import chessgame.rule.DirectionalAttackingPieceRule;
import chessgame.rule.PinnedSet;
import chessgame.rule.PinningPieceRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that implements Rook piece moving logic
 */
public final class Rook<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Rook(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Rook{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class RookRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridBoard<C, D, A, P>> implements DirectionalAttackingPieceRule<C, D, A, P, B> {
        @Override
        public Collection<D> getAttackingDirections(B board) {
            return board.getOrthogonalDirections();
        }
    }
}