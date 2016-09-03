import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by haozhending on 9/3/16.
 */
public interface Board {

    /**
     * @return empty if there is no piece located at this location;
     * or non-null value that represents the piece located at this location
     */
    Optional<Piece> getPiece(Location location);

    /**
     * @param piece The piece to set at this location, cannot be null
     * @return the previous piece at this location
     */
    Optional<Piece> setPiece(Location location, Piece piece);


    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Optional<Piece> clearPiece(Location location);

    /*
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     */
    void movePiece(Location source, Location target);

    Collection<Piece> getAllPieces();

}
