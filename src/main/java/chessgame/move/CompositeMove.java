package chessgame.move;

import chessgame.board.Cell;

/**
 * Represents a move that involves the multiple pieces
 */
public interface CompositeMove<C extends Cell> extends Move<C> {

    /**
     * A composite move must identify where is the new position of the actor's king
     * since the game mechanism must validate that a king should never move to a position that is currently being attacked
     * @return the position of the actor's king (could be identical to the old position)
     */
    C actorKingNewPosition();
}
