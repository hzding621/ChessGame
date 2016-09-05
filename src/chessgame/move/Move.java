package chessgame.move;

import chessgame.board.BoardView;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * Represents a move in the game wherein a piece is moved from A to B
 */
public interface Move<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardView<C, A, P>> {

    C getSource();

    C getTarget();
}
