package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceType;

/**
 * Classes that implement this interface depend on runtime board situation.
 * They should hold a reference to a BoardViewer that is valid through an entire game
 */
public interface RequiresBoardView<C extends Cell, P extends PieceType, B extends BoardViewer<C, P>> {

    B getBoardViewer();
}
