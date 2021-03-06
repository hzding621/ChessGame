package core.piece;

import core.board.Direction;
import core.board.GridViewer;
import core.board.Tile;

import java.util.Collection;

/**
 * Class that implements Rook piece moving logic
 */
public final class Rook<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>>
        implements Rider<C, P, D, B>, PieceRule<C,P,B> {

    @Override
    public Collection<D> getDirections(B board) {
        return board.getOrthogonalDirections();
    }
}