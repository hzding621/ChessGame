package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * Classes that implement this interface depend on runtime board situation.
 * They should hold a reference to a BoardViewer that is valid through an entire game
 */
public interface RequiresBoardView<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardViewer<C, A, P>> {

    B getBoardViewer();
}
