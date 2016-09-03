import java.util.Optional;

/**
 * Represents a regular 8x8 chess board
 * The Location variable in each method call must be an instance of SquareBoardCell object
 */
public final class SquareBoard extends AbstractBoard {

    private void checkValidLocations(Location... locations) {
        for (Location location : locations) {
            if (!(location instanceof SquareBoardCell)) {
                throw new IllegalStateException("SquareBoard only supports SquareBoardCell as Location!");
            }
        }
    }

    @Override
    public Optional<Piece> getPiece(Location location) {
        checkValidLocations(location);
        return super.getPiece(location);
    }

    @Override
    public void movePiece(Location source, Location target) {
        checkValidLocations(source, target);
        super.movePiece(source, target);
    }

    @Override
    public Optional<Piece> setPiece(Location location, Piece piece) {
        checkValidLocations(location);
        return super.setPiece(location, piece);
    }

    @Override
    public Optional<Piece> clearPiece(Location location) {
        checkValidLocations(location);
        return super.clearPiece(location);
    }
}
