package core.board;

import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents an immutable Board that can be queried.
 * Reference to classes that implements this interface should be valid through the entire lifetime of a game
 */
public interface BoardViewer<C extends Tile, P extends PieceClass> {

    /**
     * @return empty if there is no piece located at this board;
     * or non-null value that represents the piece located at this board
     */
    Optional<Piece<P>> getPiece(C tile);

    /**
     * @return all pieces on the board of one type belonging to a certain player
     */
    Collection<C> getPieceLocationsOfTypeAndPlayer(P type, Player player);

    /**
     * @return all pieces on the board of belonging to a certain player
     */
    Collection<C> getPieceLocationsOfPlayer(Player player);

    /**
     * @param tile the tile to check
     * @return whether the tile on board is occupied
     */
    boolean isOccupied(C tile);


    /**
     * @param tile the tile to check
     * @return whether the tile on board is empty
     */
    default boolean isEmpty(C tile) {
        return !isOccupied(tile);
    }

    /**
     * @param tile the tile to check
     * @param player the player
     * @return whether the tile on board is occupied by an enemy piece
     */
    boolean isEnemy(C tile, Player player);

}
