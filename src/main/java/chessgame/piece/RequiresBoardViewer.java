package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Tile;

/**
 * Classes that implement this interface depend on runtime board situation.
 * They should hold a reference to a BoardViewer that is valid through an entire game
 */
public interface RequiresBoardViewer<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>> {

    B getBoardViewer();
}
