package chessgame.view;

import chessgame.board.ChessBoardViewer;
import chessgame.game.StandardGame;
import chessgame.piece.Piece;
import chessgame.piece.StandardPieces;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the view for the chess board
 */
public class BoardView {

    private final StandardGame game;
    private final GridPane gridPane;
    private final Map<Piece<StandardPieces>, Integer> imageIndices = new HashMap<>();
    private static final Color COLOR_A =  Color.rgb(0xFF, 0xCE, 0x9E);
    private static final Color COLOR_B = Color.rgb(0xD1, 0x8B, 0x47);

    public BoardView(StandardGame game) {

        this.game = game;
        this.gridPane = createGridPane(game.getSetting().getFileLength(), game.getSetting().getRankLength());
        populatePieceImages(game);
    }

    private static GridPane createGridPane(int fileLength, int rankLength) {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Create Rows and Columns
        for (int i = 0; i < fileLength; i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);
        }
        for (int j = 0; j < rankLength; j++) {
            RowConstraints row = new RowConstraints(80);
            row.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(row);
        }

        // Create 8x8 tiles
        for (int i = 0; i < fileLength; i++) {
            for (int j = 0; j < rankLength; j ++) {
                Rectangle rectangle = new Rectangle(80, 80);
                rectangle.setFill((i + j) % 2 == 0 ? COLOR_A : COLOR_B);
                gridPane.add(rectangle, j, i);
            }
        }
        return gridPane;
    }

    private int toColumnIndex(int rank) {
        return game.getSetting().getRankLength() - 1 - rank;
    }

    private void populatePieceImages(StandardGame game) {

        ChessBoardViewer<StandardPieces> board = game.getBoard();

        game.getSetting().getPlayers().forEach(player -> {

            board.getPiecesForPlayer(player).forEach(square -> {
                int file = square.getFile().getCoordinate().getIndex();
                int rank = square.getRank().getCoordinate().getIndex();

                Piece<StandardPieces> piece = board.getPiece(square).get();

                ImageView pieceIcon = new ImageView(StandardPiecesIcon.getResource(piece.getPieceClass(), player));
                pieceIcon.setFitHeight(60);
                pieceIcon.setFitWidth(60);

                imageIndices.put(piece, gridPane.getChildren().size());
                gridPane.add(pieceIcon, file, toColumnIndex(rank));
            });
        });
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
