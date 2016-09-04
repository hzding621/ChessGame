package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceSet;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractBoard<C extends Cell, A extends PieceType, P extends Piece<A>>
        implements Board<C, A, P> {

    protected final Map<C, P> occupants = new HashMap<>();
    protected final BoardInformation<C, A, P> boardInformation = new Information();

    private final PieceSet<C, A, P> pieceSet;

    protected AbstractBoard(PieceSet<C, A, P> pieceSet) {
        this.pieceSet = pieceSet;
    }

    @Override
    public void initializeBoard() {
    }

    public Optional<P> getPiece(C cell) {
        if (!occupants.containsKey(cell)) {
            return Optional.empty();
        }
        return Optional.of(occupants.get(cell));
    }

    public Optional<P> setPiece(C cell, P piece) {
        if (piece == null) {
            throw new IllegalStateException("Cannot set board to a null pieceType");
        }
        Optional<P> previousPiece = getPiece(cell);
        occupants.put(cell, piece);
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
    }

    @Override
    public Optional<P> clearPiece(C cell) {
        Optional<P> previousPiece = getPiece(cell);
        occupants.remove(cell);


        return previousPiece;
    }


    @Override
    public Collection<PieceLocator<C>> getPiecesForPlayer(PieceType type, Player player) {
        return occupants.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getPieceClass().equals(type)
                                && e.getValue().getPlayer().equals(player))
                        .map(e -> new PieceLocator<C>(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<PieceLocator<C>> getAllPiecesForPlayer(PieceType type, Player player) {
        return occupants.entrySet()
                        .stream()
                        .map(e -> new PieceLocator<C>(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

    private class Information implements BoardInformation<C, A, P> {

        private final Map<C, Collection<C>> attackers = new HashMap<>();

        @Override
        public Collection<C> getAttackers(C attacked) {
            if (!attackers.containsKey(attacked)) {
                return Collections.emptyList();
            } else {
                return attackers.get(attacked);
            }
        }
    }

}
