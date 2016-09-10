package chessgame.move;

import chessgame.board.Cell;
import chessgame.board.MutableBoard;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell>  {

    C getInitiator();

    Player getPlayer();

    <P extends PieceClass, M extends MutableBoard<C, P, M>> BoardTransition<C, P, M> getTransition();
}
