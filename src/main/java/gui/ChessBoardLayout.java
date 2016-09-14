package gui;

import core.board.ChessBoardViewer;
import core.game.ChessGame;
import core.piece.Piece;
import core.piece.PieceClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

/**
 * This class contains the view for the chess board
 */
public class ChessBoardLayout<P extends PieceClass> {

    private final ChessGame<P> game;
    private final PiecesIcon<P> icons;
    private final GridPane layout;

    public ChessBoardLayout(ChessGame<P> game, PiecesIcon<P> icons) {

        this.game = game;
        this.icons = icons;
        this.layout = createGrid(game.getSetting().getFileLength(),
                game.getSetting().getRankLength(), ColorScheme.STANDARD);
        populatePieceImages();
    }

    private static GridPane createGrid(int fileLength, int rankLength, ColorScheme colorScheme) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Create Rows and Columns
        for (int i = 0; i < fileLength; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(80);
            column.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(column);
        }
        for (int j = 0; j < rankLength; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(80);
            row.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(row);
        }

        // Create 8x8 tiles
        for (int i = 0; i < fileLength; i++) {
            for (int j = 0; j < rankLength; j ++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(80);
                rectangle.setHeight(80);
                rectangle.setFill((i + j) % 2 == 0 ? colorScheme.dark() : colorScheme.light());
                rectangle.setId(idOfTile(i, j));
                grid.add(rectangle, i, toRowIndex(j, rankLength));
            }
        }
        return grid;
    }

    private void populatePieceImages() {

        ChessBoardViewer<P> board = game.getBoard();
        game.getSetting().getPlayers().forEach(player -> board.getPieceLocationsOfPlayer(player).forEach(square -> {

            int file = square.getFile().getCoordinate().getIndex();
            int rank = square.getRank().getCoordinate().getIndex();

            Piece<P> piece = board.getPiece(square).get();

            ImageView pieceIcon = new ImageView();
            pieceIcon.setImage(new Image(icons.getResource(piece.getPieceClass(), player)));
            pieceIcon.setFitHeight(60);
            pieceIcon.setFitWidth(60);
            pieceIcon.setId(idOfPiece(piece));
            layout.add(pieceIcon, file, toRowIndex(rank, game.getSetting().getRankLength()));

        }));
    }

    private static int toRowIndex(int rank, int rankLength) {
        return rankLength - 1 - rank;
    }

    private static <P extends PieceClass> String idOfPiece(Piece<P> piece) {
        return "Piece[" + piece.toString() + "]";
    }

    private static String idOfTile(int file, int rank) {
        return "TILE[" + file + "," + rank + "]";
    }

    private Node getPieceNode(Piece<P> piece) {
        return layout.lookup("#" + idOfPiece(piece));
    }

    public GridPane getLayout() {
        return layout;
    }
}
