package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.StepSize;

/**
 * Class that implements Knight piece moving logic
 */

public final class Knight<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> implements Leaper<C, P, D, B> {

    @Override
    public StepSize getDistance() {
        return StepSize.of(1, 2);
    }
}