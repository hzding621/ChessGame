package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a chess board in the abstract way. Stores a mapping from cells to pieces.
 * Makes no assumptions about how cells are connected to each other.
 */
abstract class AbstractBoard<C extends Cell, P extends PieceClass, V extends BoardViewer<C, P>, T extends AbstractBoard<C, P, V, T>>
        implements MutableBoard<C, P, T>, Previewer<C, P, V, T> {

    static class Instance<C extends Cell, P extends PieceClass>
            extends AbstractBoard<C, P, Instance<C, P>, Instance<C, P>> {

        protected Instance(Map<C, Piece<P>> occupants) {
            super(occupants);
        }

        public static <C extends Cell, P extends PieceClass> Instance<C, P> create(GameSetting<C, P> gameSetting) {
            return new Instance<>(gameSetting.constructPiecesByStartingPosition());
        }

        @Override
        public TransitionResult<C, P> apply(BoardTransition<C, P, Instance<C, P>> boardTransition) {
            return boardTransition.apply(this);
        }

        @Override
        public Instance<C, P> preview(BoardTransition<C, P, Instance<C, P>> transition) {
            Instance<C, P> newInstance = new Instance<>(this.occupants);
            newInstance.apply(transition);
            return newInstance;
        }
    }

    final Map<C, Piece<P>> occupants = Maps.newTreeMap();

    AbstractBoard(Map<C, Piece<P>> occupants) {

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
    public boolean isOccupied(C cell) {
        return occupants.containsKey(cell);
    }

    @Override
    public boolean isEnemy(C cell, Player player) {
        return isOccupied(cell) && !getPiece(cell).get().getPlayer().equals(player);
    }

    @Override
    public Optional<Piece<P>> getPiece(C cell) {
        return Optional.ofNullable(occupants.get(cell));
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
