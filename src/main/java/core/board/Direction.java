package core.board;

/**
 * Represents a direction any piece can move on a board
 */
public interface Direction<T extends Direction<T>> {

    /**
     * @return rotate to the next clockwise direction by a unit (e.g. a unit in TwoDimension is 45 degree).
     */
    T nextClockWise();

    /**
     * @return rotate to the next counter clockwise direction by a unit (e.g. a unit in TwoDimension is 45 degree).
     */
    T nextCounterClockWise();


    /**
     * @return the reverse of the current direction
     */
    T reverse();
}