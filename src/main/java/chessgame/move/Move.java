package chessgame.move;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell> {

    C getSource();

    C getTarget();

    <P extends PieceClass> BoardTransition<C, P> getTransition();
}
