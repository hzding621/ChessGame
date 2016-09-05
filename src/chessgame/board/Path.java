package chessgame.board;

import java.util.Arrays;
import java.util.List;

/**
 * Represent a series of step a piece takes move.
 */
public final class Path<C extends Cell> {

    private final List<C> path;

    private Path(List<C> path) {
        this.path = path;
    }

    public List<C> getPath() {
        return path;
    }

    public static <C extends Cell> Path<C> of(List<C> positions) {
        return new Path(positions);
    }

    public static <C extends Cell> Path<C> of(C... positions) {
        return new Path<>(Arrays.asList(positions));
    }
}
