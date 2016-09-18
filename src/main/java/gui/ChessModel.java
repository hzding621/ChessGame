package gui;

import core.board.ChessBoard;
import core.board.Square;
import core.game.ChessGame;
import core.game.GameSetting;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;
import javafx.beans.property.MapPropertyBase;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public Stream<PieceModel<P>> streamAllPieces() {
        return game.getSetting().getPlayers().stream().flatMap(player ->
                game.getBoard().getPieceLocationsOfPlayer(player).stream().map(square -> {
                    Piece<P> p = game.getBoard().getPiece(square).get();
                    return new PieceModel<>(p, square, player);
                }));
    }

    public int getFileLength() {
        return game.getSetting().getFileLength();
    }

    public int getRankLength() {
        return game.getSetting().getRankLength();
    }

    public static class PieceModel<P extends PieceClass> {
        private final Piece<P> piece;
        private final Square square;
        private final Player player;

        private PieceModel(Piece<P> piece, Square square, Player player) {
            this.piece = piece;
            this.square = square;
            this.player = player;
        }

        public P getType() {
            return piece.getPieceClass();
        }

        public int getFile() {
            return square.getFile().getCoordinate().getIndex();
        }

        public int getRank() {
            return square.getRank().getCoordinate().getIndex();
        }

        public Player getPlayer() {
            return player;
        }

        public String pieceId() {
            return piece.toString();
        }
    }

}
