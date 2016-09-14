package core.move;

import core.board.Board;
import core.board.Tile;
import core.piece.PieceClass;

/**
 * Functional interface that represents a board transition applying on board and returning transition history
 */
@FunctionalInterface
public interface BoardTransition<C extends Tile, P extends PieceClass, T extends Board<C, P>> {

    TransitionResult<C, P> transition(T board);
}
