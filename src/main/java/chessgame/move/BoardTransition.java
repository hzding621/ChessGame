package chessgame.move;

import chessgame.board.MutableBoard;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;

import java.util.function.Function;

/**
 * Functional interface that represents a board transition applying on board and returning transition history
 */
@FunctionalInterface
public interface BoardTransition<C extends Cell, P extends PieceClass, T extends MutableBoard<C, P, T>>
        extends Function<T, TransitionResult<C, P>> {
}
