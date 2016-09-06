package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

/**
 * Represents a board in a chess game, which can be quried as well as updated
 */
public interface Board<C extends Cell, P extends PieceClass> extends BoardViewer<C, P> {

    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Piece<P> clearPiece(C cell);

    /**
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     * @param source cell to move from
     * @param target cell to move to
     * @return The moved piece
     */
    Piece<P> movePiece(C source, C target);
}
