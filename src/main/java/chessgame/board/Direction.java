package chessgame.board;

/**
 * Represents a direction any piece can move on a board
 */
public interface Direction<T extends Direction<T>> {

    /**
     * @return the next direction clockwise, counting both diagonal and orthogonal!
     */
    T nextClockWise();

    /**
     * @return the next direction counterclockwise, , counting both diagonal and orthogonal!
     */
    T nextCounterClockWise();


    /**
     * @return the reverse of the current direction
     */
    T reverse();
}