package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;
import chessgame.rule.RangeAttackPieceRule;

import java.util.Collection;

/**
 * Class that implements Queen piece moving logic
 */
public final class Queen<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Queen(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Queen{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class QueenRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridViewer<C, D, A, P>> extends AbstractPieceRule<C, A, P, B>
            implements RangeAttackPieceRule<C, D, A, P, B> {

        public QueenRule(B gridViewer) {
            super(gridViewer);
        }

        @Override
        public Collection<D> getAttackingDirections() {
            return boardViewer.getAllDirections();
        }
    }
}