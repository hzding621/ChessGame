package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;

public interface Board<C extends Cell, A extends PieceType, P extends Piece<A>> {

    /**
     * @return empty if there is no piece located at this board;
     * or non-null value that represents the piece located at this board
     */
    Optional<P> getPiece(C cell);

    /**
     * @param piece The pieceType to set at this board, cannot be null
     * @return the previous pieceType at this board
     */
    Optional<P> setPiece(C cell, P piece);

    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Optional<P> clearPiece(C cell);

    /**
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     * @param source cell to move from
     * @param target cell to move to
     * @return The moved piece
     */
    P movePiece(C source, C target);

    /**
     * @return all pieces on the board of one type belonging to a certain player
     */
    Collection<PieceLocator<C, A, P>> getPiecesForPlayer(PieceType type, Player player);

    /**
     * @return all pieces on the board of belonging to a certain player
     */
    Collection<PieceLocator<C, A, P>> getAllPiecesForPlayer(Player player);

    Collection<PieceLocator<C, A, P>> getAllPieces();

    boolean isOccupied(C cell);

    boolean isEnemy(C cell, Player player);

    void initializeBoard();
}
