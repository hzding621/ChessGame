package chessgame.board;

import chessgame.piece.Piece;

import java.util.Collection;
import java.util.Optional;

public interface Board<C extends Cell> {

    /**
     * @return empty if there is no piece located at this board;
     * or non-null value that represents the piece located at this board
     */
    Optional<Piece> getPiece(C cell);

    /**
     * @param pieceType The pieceType to set at this board, cannot be null
     * @return the previous pieceType at this board
     */
    Optional<Piece> setPiece(C cell, Piece pieceType);

    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Optional<Piece> clearPiece(C cell);

    /*
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     */
    void movePiece(C source, C target);

    /**
     * @return all directions a piece can potentially move that are supported by this board implementation
     */
    Collection<Direction> getAllDirections();

    /**
     * @return all cells that are attacking `attacked`
     */
    Collection<C> getAttackers(C attacked);
}
