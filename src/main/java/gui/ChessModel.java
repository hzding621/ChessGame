package gui;

import core.board.Square;
import core.game.ChessGame;
import core.move.Move;
import core.piece.Piece;
import core.piece.PieceClass;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Model class in MVC framework. Containing all the state of the game.
 * In this case, it should mainly just contain the reference to the Game class
 */
public class ChessModel<P extends PieceClass> {

    private final ChessGame<P> game;
    private final ObservableMap<Square, PieceId<P>> observableMap = FXCollections.observableHashMap();
    private final ObjectProperty<Square> selectedTile = new SimpleObjectProperty<>();

    private final ObservableList<Square> movableTiles = FXCollections.observableArrayList();

    public ObservableMap<Square, PieceId<P>> getObservableMap() {
        return observableMap;
    }

    public ObjectProperty<Square> getSelectedTile() {
        return selectedTile;
    }

    public ObservableList<Square> getMovableTiles() {
        return movableTiles;
    }

    public ChessModel(ChessGame<P> game) {
        this.game = game;
    }

    public Stream<PieceId<P>> piecesConfiguration() {
        return game.getSetting().constructPiecesByStartingPosition().values().stream().map(PieceId::new);
    }

    public void refreshAllPieces() {
        game.getBoard().getMap().forEach((square, piece) -> observableMap.put(square, PieceId.of(piece)));
    }

    private void updateMovableTiles() {
        if (selectedTile.getValue() != null) {
            movableTiles.setAll(game.availableMovesFrom(selectedTile.getValue()).stream()
                    .map(Move::getDestination).collect(Collectors.toList()));
        } else {
            movableTiles.clear();
        }
    }

    public void setSelectedSquare(int file, int rank) {
        Square square = game.getBoard().getGridTileBuilder().at(file, rank);
        if (game.getBoard().isOccupied(square) && !game.getBoard().isEnemy(square, game.getActor())) {
            selectedTile.setValue(square);
        } else {
            selectedTile.setValue(null);
        }
        updateMovableTiles();
    }

    public void setSelectedTile(PieceId<P> pieceId) {
        observableMap.entrySet().stream().filter(entry -> entry.getValue().equals(pieceId)).forEach(entry -> {
            setSelectedSquare(entry.getKey().getFile().getCoordinate().getIndex(),
                    entry.getKey().getRank().getCoordinate().getIndex());
        });
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
