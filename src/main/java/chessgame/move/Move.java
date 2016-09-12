package chessgame.move;

import chessgame.board.Cell;
import chessgame.board.Board;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell, P extends PieceClass>  {

    /**
     * @return the initiating piece of this move
     */
    C getInitiator();

    /**
     * @return the initiating player of this move
     */
    Player getPlayer();

    /**
     * @return the transition function that implements this move
     */
    <M extends Board<C, P>> BoardTransition<C, P, M> getTransition();
}
