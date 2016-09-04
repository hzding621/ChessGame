package chessgame.board;

import chessgame.piece.Piece;

import java.util.*;

public abstract class AbstractBoard<C extends Cell> implements Board<C> {

    private final Map<C, Piece> occupants = new HashMap<>();
    private final Map<C, Collection<C>> attackers = new HashMap<>();

    public Optional<Piece> getPiece(C cell) {
        if (!occupants.containsKey(cell)) {
            return Optional.empty();
        }
        return Optional.of(occupants.get(cell));
    }

    public Optional<Piece> setPiece(C cell, Piece pieceType) {
        if (pieceType == null) {
            throw new IllegalStateException("Cannot set board to a null pieceType");
        }
        Optional<Piece> previousPiece = getPiece(cell);
        occupants.put(cell, pieceType);
        return previousPiece;
    }

    public void movePiece(C source, C target) {
        if (!occupants.containsKey(source)) {
            throw new IllegalStateException("Source position is empty!");
        }
        if (occupants.containsKey(target)) {
            throw new IllegalStateException("Target position is not empty!");
        }
        occupants.put(target, occupants.get(source));
        occupants.remove(source);

        // Update attackers

    }

    @Override
    public Optional<Piece> clearPiece(C cell) {
        Optional<Piece> previousPiece = getPiece(cell);
        occupants.remove(cell);


        return previousPiece;
    }

    @Override
    public Collection<C> getAttackers(C attacked) {
        if (!attackers.containsKey(attacked)) {
            return Collections.emptyList();
        } else {
            return attackers.get(attacked);
        }
    }



}
