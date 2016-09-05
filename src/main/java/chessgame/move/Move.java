package chessgame.move;

import chessgame.board.Cell;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell> {

    C getSource();

    C getTarget();
}