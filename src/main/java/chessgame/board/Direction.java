package chessgame.board;

/**
 * Represents a direction any piece can move on a board
 */
public interface Direction<T extends Direction<T>> {

    T getClockwise();

    T getCounterClockwise();
}