package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceSet;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractBoard<C extends Cell, A extends PieceType, P extends Piece<A>> implements Board<C, A, P> {

    protected final Map<C, P> occupants;

    protected AbstractBoard(PieceSet<C, A, P> pieceSet) {
        this.occupants = new TreeMap<>(
                pieceSet.constructAllPieces()
                        .stream()
                        .collect(Collectors.toMap(PieceLocator::getCell, PieceLocator::getPiece)));
    }

    @Override
    public boolean isOccupied(C cell) {
        return occupants.get(cell) != null;
    }

    @Override
    public boolean isEnemy(C cell, Player player) {
        return occupants.containsKey(cell) && !occupants.get(cell).getPlayer().equals(player);
    }

    @Override
    public Optional<P> getPiece(C cell) {
        if (!occupants.containsKey(cell)) {
            return Optional.empty();
        }
        return Optional.of(occupants.get(cell));
    }

    @Override
    public P movePiece(C source, C target) {
        if (!occupants.containsKey(source)) {
            throw new IllegalStateException("Source position is empty!");
        }
        if (occupants.containsKey(target)) {
            throw new IllegalStateException("Target position is not empty!");
        }
        occupants.put(target, occupants.get(source));
        return occupants.remove(source);
    }

    @Override
    public Optional<P> clearPiece(C cell) {
        Optional<P> previousPiece = getPiece(cell);
        occupants.remove(cell);
        return previousPiece;
    }

    @Override
    public Collection<PieceLocator<C, A, P>> getPiecesForPlayer(PieceType type, Player player) {
        return occupants.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getPieceClass().equals(type)
                                && e.getValue().getPlayer().equals(player))
                        .map(e -> PieceLocator.of(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<PieceLocator<C, A, P>> getAllPiecesForPlayer(Player player) {
        return occupants.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getPlayer().equals(player))
                        .map(e -> PieceLocator.of(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }
}
