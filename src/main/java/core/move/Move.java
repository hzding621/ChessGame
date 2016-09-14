package core.move;

import core.board.Tile;
import core.board.Board;
import core.piece.PieceClass;
import core.player.Player;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Tile, P extends PieceClass>  {

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
