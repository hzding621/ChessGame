package chessgame.move;

import chessgame.board.Cell;

import java.util.Arrays;
import java.util.List;

/**
 * Represent a series of step a piece takes move.
 */
public final class MovePath<C extends Cell> {

    private final List<C> path;

    private MovePath(List<C> path) {
        this.path = path;
    }

    public List<C> getPath() {
        return path;
    }

    public static <C extends Cell> MovePath<C> of(List<C> positions) {
        return new MovePath(positions);
    }

    public static <C extends Cell> MovePath<C> of(C... positions) {
        return new MovePath<>(Arrays.asList(positions));
    }
}
