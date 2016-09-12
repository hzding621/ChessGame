package chessgame.move;

import chessgame.board.Board;
import chessgame.board.Tile;
import chessgame.piece.PieceClass;

/**
 * Functional interface that represents a board transition applying on board and returning transition history
 */
@FunctionalInterface
public interface BoardTransition<C extends Tile, P extends PieceClass, T extends Board<C, P>> {

    TransitionResult<C, P> transition(T board);
}
