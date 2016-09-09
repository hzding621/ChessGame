package chessgame.move;

import chessgame.board.Board;
import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;

/**
 * Represent a series of mutations applicable the board
 */
public interface BoardMutation<C extends Cell, P extends PieceClass> {

    <B extends Board<C, P>> void mutate(B board);

    <B extends BoardViewer<C, P>> B preview(B boardViewer);
}
