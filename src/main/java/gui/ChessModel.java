package gui;

import core.board.Square;
import core.game.ChessGame;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.stream.Stream;

/**
 * Model class in MVC framework. Containing all the state of the game.
 * In this case, it should mainly just contain the reference to the Game class
 */
public class ChessModel<P extends PieceClass> {

    private final ChessGame<P> game;

    private final ObservableMap<Square, Piece<P>> observableMap = FXCollections.observableHashMap();

    public ObservableMap<Square, Piece<P>> getObservableMap() {
        return observableMap;
    }

    public ChessModel(ChessGame<P> game) {
        this.game = game;
    }

    public Stream<PieceId<P>> piecesConfiguration() {
        return game.getSetting().constructPiecesByStartingPosition().values().stream().map(PieceId::new);
    }

    public void refreshAllPieces() {
        this.observableMap.putAll(game.getBoard().getMap());
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

        public PieceId(Piece<P> piece) {
            this.id = piece.toString();
            this.shortId = piece.getPieceClass() + "_" + piece.getPlayer();
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
    }

}
