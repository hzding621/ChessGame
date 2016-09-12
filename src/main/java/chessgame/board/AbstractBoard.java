package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents a chess board in the abstract way. Stores a mapping from tiles to pieces.
 * Makes no assumptions about how tiles are connected to each other.
 */
abstract class AbstractBoard<C extends Tile, P extends PieceClass> implements Board<C, P> {

    protected final Map<C, Piece<P>> occupants = new TreeMap<>();

    protected AbstractBoard(Map<C, Piece<P>> occupants) {

        // There is no magic here, create copies !!!
        this.occupants.putAll(occupants);
    }

    @Override
    public Piece<P> movePiece(C source, C target) {
        Piece<P> movedPiece = clearPiece(source);
        addPiece(target, movedPiece);
        return movedPiece;
    }

    @Override
    public Piece<P> clearPiece(C position) {
        Optional<Piece<P>> previousPiece = getPiece(position);
        if (!previousPiece.isPresent()) {
            throw new IllegalStateException("The position " + position + " is empty!");
        }
        occupants.remove(position);
        return previousPiece.get();
    }

    @Override
    public void addPiece(C position, Piece<P> piece) {
        if (isOccupied(position)) {
            throw new IllegalStateException("The position " + position + " is occupied!");
        }
       occupants.put(position, piece);
    }

    @Override
    public boolean isOccupied(C tile) {
        return occupants.containsKey(tile);
    }

    @Override
    public boolean isEnemy(C tile, Player player) {
        return isOccupied(tile) && !getPiece(tile).get().getPlayer().equals(player);
    }

    @Override
    public Optional<Piece<P>> getPiece(C tile) {
        return Optional.ofNullable(occupants.get(tile));
    }

    @Override
    public Collection<C> getPiecesOfTypeForPlayer(P type, Player player) {
        return occupants.entrySet()
                .stream()
                .filter(e -> e.getValue().getPieceClass().equals(type) && e.getValue().getPlayer().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<C> getPiecesForPlayer(Player player) {
        return occupants.entrySet()
                .stream()
                .filter(e -> e.getValue().getPlayer().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
