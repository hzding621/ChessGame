package chessgame.board;

import java.util.Optional;

/**
 * Created by haozhending on 9/4/16.
 */
public interface GridCellFactory<C extends Cell, D extends Direction> {

    Optional<SquareCell> moveOnce(C cell, D direction);
}
