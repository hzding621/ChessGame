package chessgame.board;

/**
 * Represents a direction any piece can move on a board
 */
public interface Direction {

    Direction getClockwise();

    Direction getCounterClockwise();
}