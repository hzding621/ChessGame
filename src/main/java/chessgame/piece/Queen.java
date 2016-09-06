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
public final class Queen<C extends Cell, P extends PieceClass> extends AbstractPiece<C, P> {

    public Queen(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Queen{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class QueenRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements RangeAttackPieceRule<C, P, D, B> {

        public QueenRule(B gridViewer) {
            super(gridViewer);
        }

        @Override
        public Collection<D> getAttackingDirections() {
            return boardViewer.getAllDirections();
        }
    }
}