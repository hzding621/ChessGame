package chessgame.board;

import java.util.Optional;

/**
 * Factory interface for constructing cells used for grid
 */
public interface GridCellFactory<C extends Cell, D extends Direction> {

    Optional<C> of(String file, String rank);

    Optional<C> of(int fileIndex, int rankIndex);

    Optional<C> moveOnce(C cell, D direction);
}
