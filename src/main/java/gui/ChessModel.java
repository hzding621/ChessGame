package gui;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import core.board.Square;
import core.game.ChessGame;
import core.move.Move;
import core.piece.Piece;
import core.piece.PieceClass;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Model class in MVC framework. Containing all the state of the game.
 * In this case, it should mainly just contain the reference to the Game class
 */
public class ChessModel<P extends PieceClass> {

    private final Supplier<ChessGame<P>> gameSupplier;
    private final ObservableMap<Square, PieceId<P>> observableMap = FXCollections.observableHashMap();
    private final ObjectProperty<Square> selectedTile = new SimpleObjectProperty<>();
    private final ObservableList<Square> movableTiles = FXCollections.observableArrayList();
    private final ObjectProperty<Square> attackedKing = new SimpleObjectProperty<>();
    private final IntegerProperty totalMoves = new SimpleIntegerProperty(0);

    private ChessGame<P> game;

    public void newGame() {
        game = gameSupplier.get();
        selectedTile.setValue(null);
        attackedKing.setValue(null);
        movableTiles.clear();
        totalMoves.setValue(0);
        pullPiecesLocationsUpdate();
    }

    public IntegerProperty totalMovesProperty() {
        return totalMoves;
    }

    public ChessModel(Supplier<ChessGame<P>> gameSupplier) {
        this.gameSupplier = gameSupplier;
        this.game = gameSupplier.get();
    }

    public ObservableMap<Square, PieceId<P>> observableMap() {
        return observableMap;
    }

    public ObjectProperty<Square> attackedKingProperty() {
        return attackedKing;
    }

    public ObjectProperty<Square> selectedTileProperty() {
        return selectedTile;
    }

    public ObservableList<Square> movableTilesProperty() {
        return movableTiles;
    }

    private void updateMovableTiles() {
        if (selectedTile.getValue() != null) {
            movableTiles.setAll(game.availableMovesFrom(selectedTile.getValue()).stream()
                    .map(Move::getDestination).collect(Collectors.toList()));
        } else {
            movableTiles.clear();
        }
    }

    private void startMove(Square source, Square target) {
        Move<Square, P> move = Iterables.tryFind(game.availableMovesFrom(source),
                m -> m.getDestination().equals(target)).get();
        game.move(move);
        pullPiecesLocationsUpdate();
        updateKingCheckHighlight();
        totalMoves.setValue(totalMoves.get() + 1);
    }

    public void undoLastRound() {
        game.undoLastRound();
        pullPiecesLocationsUpdate();
        updateKingCheckHighlight();
        totalMoves.setValue(totalMoves.get() - 2);
    }

    public void selectSquare(int file, int rank) {
        Square square = game.getBoard().getGridTileBuilder().at(file, rank);
        if (movableTiles.contains(square)) {
            startMove(selectedTile.get(), square);
            selectedTile.setValue(null);
        }
        else if (game.getBoard().isOccupied(square) && !game.getBoard().isEnemy(square, game.getActor())) {
            selectedTile.setValue(square);
        } else {
            selectedTile.setValue(null);
        }
        updateMovableTiles();
    }

    public void selectSquareByPiece(PieceId<P> pieceId) {
        Square square = Iterables.tryFind(observableMap.entrySet(), e -> e.getValue().equals(pieceId)).get().getKey();
        selectSquare(square.getFile().getCoordinate().getIndex(), square.getRank().getCoordinate().getIndex());
    }

    private void updateKingCheckHighlight() {
        if (!game.getRuntimeInformation().getAttackInformation().getCheckers().isEmpty()) {
            Square kingPosition = game.getRuntimeInformation().getPieceInformation()
                    .locateKing(game.getRuntimeInformation().getPlayerInformation().getActor());
            attackedKing.setValue(kingPosition);
        } else {
            attackedKing.setValue(null);
        }
    }

    public void pullPiecesLocationsUpdate() {
        Map<Square, Piece<P>> updatedMap = game.getBoard().getMap();
        List<Square> removedPieces = observableMap.keySet()
                .stream().filter(square -> !updatedMap.containsKey(square)).collect(Collectors.toList());
        removedPieces.forEach(observableMap::remove);
        updatedMap.forEach((square, piece) -> observableMap.put(square, PieceId.of(piece)));
    }

    public Stream<PieceId<P>> piecesConfiguration() {
        return game.getSetting().constructPiecesByStartingPosition().values().stream().map(PieceId::new);
    }

    public int getFileLength() {
        return game.getSetting().getFileLength();
    }

    public int getRankLength() {
        return game.getSetting().getRankLength();
    }

    public static class PieceId<P extends PieceClass> {
        private final String id;
        private final String shortId;

        private PieceId(Piece<P> piece) {
            this.id = piece.toString();
            this.shortId = piece.getPieceClass() + "_" + piece.getPlayer();
        }

        public static <P extends PieceClass> PieceId<P> of(Piece<P> piece) {
            return new PieceId<>(piece);
        }

        public String getId() {
            return id;
        }

        public String getShortId() {
            return shortId;
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PieceId<?> pieceId = (PieceId<?>) o;

            return id != null ? id.equals(pieceId.id) : pieceId.id == null;

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }


}
