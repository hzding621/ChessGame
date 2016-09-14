package core.board;

import core.piece.Piece;
import core.piece.PieceClass;

/**
 * Represents a board in a chess game, which can be queried as well as updated
 */
public interface Board<C extends Tile, P extends PieceClass> extends BoardViewer<C, P> {

    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Piece<P> clearPiece(C position);

    /**
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     * @param source tile to move from
     * @param target tile to move to
     * @return The moved piece
     */
    Piece<P> movePiece(C source, C target);

    /**
     * Add a piece at the given position
     */
    void addPiece(C position, Piece<P> piece);
}
