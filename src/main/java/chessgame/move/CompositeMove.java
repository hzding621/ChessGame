package chessgame.move;

import chessgame.board.Cell;

/**
 * Represents a move that involves the multiple pieces
 */
public abstract class CompositeMove<C extends Cell> implements Move<C>, Comparable<CompositeMove<C>> {

}
