package chessgame.board;

/**
 * Represents a direction any piece can move on a board
 */
public interface Direction<T extends Direction<T>> {

    /**
     * @return the next direction clockwise, counting both diagonal and orthogonal!
     */
    T nextCloseWise();

    /**
     * @return the next direction counterclockwise, , counting both diagonal and orthogonal!
     */
    T nextCounterCloseWise();
}