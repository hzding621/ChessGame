package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.rule.Rider;

import java.util.Collection;

/**
 * Class that implements Queen piece moving logic
 */
public final class Queen<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>>
        implements Rider<C, P, D, B>, chessgame.rule.PieceRule<C,P,B> {

    @Override
    public Collection<D> getAttackingDirections(B board) {
        return board.getAllDirections();
    }
}