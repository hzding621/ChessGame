package chessgame.piece;

import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Cell;
import chessgame.player.Player;
import chessgame.rule.RangeAttackPieceRule;

import java.util.Collection;

/**
 * Class that implements Rook piece moving logic
 */
public final class Rook<P extends PieceClass> extends AbstractPiece<P> {

    public Rook(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Rook{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class RookRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements RangeAttackPieceRule<C, P, D, B> {

        public RookRule(B gridViewer) {
            super(gridViewer);
        }

        @Override
        public Collection<D> getAttackingDirections() {
            return boardViewer.getOrthogonalDirections();
        }
    }
}