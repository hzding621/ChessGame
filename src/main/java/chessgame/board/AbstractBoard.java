package chessgame.board;

import chessgame.piece.Piece;
import chessgame.game.GameSetting;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.BiMap;
import com.google.common.collect.Table;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a chess board in the abstract way. Stores a mapping from cells to pieces.
 * Makes no assumptions about how cells are connected to each other.
 */
public class AbstractBoard<C extends Cell, P extends PieceClass> implements Board<C, P> {

    protected final Map<C, Piece<P>> occupants;

    protected AbstractBoard(GameSetting<C, P> gameSetting) {
        this.occupants = new TreeMap<>(
                gameSetting.constructAllPieces()
                        .stream()
                        .collect(Collectors.toMap(PieceLocator::getCell, PieceLocator::getPiece)));
    }

    @Override
    public boolean isOccupied(C cell) {
        return occupants.containsKey(cell);
    }

    @Override
    public boolean isEnemy(C cell, Player player) {
        return occupants.containsKey(cell) && !occupants.get(cell).getPlayer().equals(player);
    }

    @Override
    public Optional<Piece<P>> getPiece(C cell) {
        if (!occupants.containsKey(cell)) {
            return Optional.empty();
        }
        return Optional.of(occupants.get(cell));
    }

    @Override
    public Piece<P> movePiece(C source, C target) {
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
    public Piece<P> clearPiece(C cell) {
        Optional<Piece<P>> previousPiece = getPiece(cell);
        if (!previousPiece.isPresent()) {
            throw new IllegalStateException("Try to clear a cell that is already empty!");
        }
        occupants.remove(cell);
        return previousPiece.get();
    }

    @Override
    public Collection<C> getPiecesForPlayer(P type, Player player) {
        return occupants.entrySet()
                .stream()
                .filter(e -> e.getValue().getPieceClass().equals(type) && e.getValue().getPlayer().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<C> getAllPiecesForPlayer(Player player) {
        return occupants.entrySet()
                .stream()
                .filter(e -> e.getValue().getPlayer().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
