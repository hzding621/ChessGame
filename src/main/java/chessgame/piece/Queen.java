package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import chessgame.rule.Rider;

import java.util.Collection;

/**
 * Class that implements Queen piece moving logic
 */
public final class Queen<P extends PieceClass> extends AbstractPiece<P> {

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
    public static final class QueenRule<C extends Cell, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements Rider<C, P, D, B> {

        @Override
        public Collection<D> getAttackingDirections(B board) {
            return board.getAllDirections();
        }
    }
}