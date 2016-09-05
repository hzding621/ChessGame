package chessgame.move;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell> {

    C getSource();

    C getTarget();
}
