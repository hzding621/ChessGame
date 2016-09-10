package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents an immutable Board that can be queried.
 * Reference to classes that implements this interface should be valid through the entire lifetime of a game
 */
public interface BoardViewer<C extends Cell, P extends PieceClass> {

    /**
     * @return empty if there is no piece located at this board;
     * or non-null value that represents the piece located at this board
     */
    Optional<Piece<P>> getPiece(C cell);

    /**
     * @return all pieces on the board of one type belonging to a certain player
     */
    Collection<C> getPiecesOfTypeForPlayer(P type, Player player);

    /**
     * @return all pieces on the board of belonging to a certain player
     */
    Collection<C> getPiecesForPlayer(Player player);

    /**
     * @param cell the cell to check
     * @return whether the cell on board is occupied
     */
    boolean isOccupied(C cell);

    /**
     * @param cell the cell to check
     * @param player the player
     * @return whether the cell on board is occupied by an enemy piece
     */
    boolean isEnemy(C cell, Player player);




}
