import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class AbstractBoard implements Board {

    private final Map<Location, Piece> positions = new HashMap<>();

    public Optional<Piece> getPiece(Location location) {
        if (!positions.containsKey(location)) {
            return Optional.empty();
        }
        return Optional.of(positions.get(location));
    }

    public Optional<Piece> setPiece(Location location, Piece piece) {
        if (piece == null) {
            throw new IllegalStateException("Cannot set location to a null piece");
        }
        Optional<Piece> previousPiece = getPiece(location);
        positions.put(location, piece);
        return previousPiece;
    }

    public void movePiece(Location source, Location target) {
        if (!positions.containsKey(source)) {
            throw new IllegalStateException("Source position is empty!");
        }
        if (positions.containsKey(target)) {
            throw new IllegalStateException("Target position is not empty!");
        }
        positions.put(target, positions.get(source));
        positions.remove(source);
    }

    public Optional<Piece> clearPiece(Location location) {
        Optional<Piece> previousPiece = getPiece(location);
        positions.remove(location);
        return previousPiece;
    }

    public Collection<Piece> getAllPieces() {
        return positions.values();
    }
}
