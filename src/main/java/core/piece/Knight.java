package core.piece;

import core.board.Tile;
import core.board.Direction;
import core.board.GridViewer;
import core.board.StepSize;

/**
 * Class that implements Knight piece moving logic
 */

public final class Knight<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> implements Leaper<C, P, D, B> {

    @Override
    public StepSize getDistance() {
        return StepSize.of(1, 2);
    }
}