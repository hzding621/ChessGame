package chessgame.board;

import java.util.Optional;

/**
 * Factory interface for constructing cells used for grid
 */
public interface GridCellFactory<C extends Cell, D extends Direction> {

    Optional<SquareCell> moveOnce(C cell, D direction);
}
