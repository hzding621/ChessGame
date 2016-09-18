package gui;

import core.piece.PieceClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class contains the view for the chess board
 */
public class ChessController<P extends PieceClass> implements Initializable {

    private static final int SQUARE_SIZE = 60;
    private final ChessModel<P> model;
    private final PiecesIcon<P> icons;

    @FXML
    GridPane grid;

    public ChessController(ChessModel<P> model, PiecesIcon<P> icons) {
        this.model = model;
        this.icons = icons;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(model.getFileLength(), model.getRankLength(), ColorScheme.STANDARD);
        populatePieceImages();
    }

    /**
     * Initialize Grid with certain file length and rank length and a color scheme
     */
    private void initializeGrid(int fileLength, int rankLength, ColorScheme colorScheme) {

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Create Rows and Columns
        for (int i = 0; i < fileLength; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(SQUARE_SIZE);
            column.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(column);
        }
        for (int j = 0; j < rankLength; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(SQUARE_SIZE);
            row.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(row);
        }

        // Create 8x8 tiles
        for (int i = 0; i < fileLength; i++) {
            for (int j = 0; j < rankLength; j ++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(SQUARE_SIZE);
                rectangle.setHeight(SQUARE_SIZE);
                rectangle.setFill((i + j) % 2 == 0 ? colorScheme.dark() : colorScheme.light());
                rectangle.setId(idOfTile(i, j));
                grid.add(rectangle, i, toRowIndex(j, rankLength));
            }
        }
    }

    /**
     * Add piece as  Rectangles (JavaFX resource) to the grid pane
     */
    private void populatePieceImages() {

        model.streamAllPieces().forEach(piece -> {
            ImageView pieceIcon = new ImageView();
            pieceIcon.setImage(new Image(icons.getResource(piece.getType(), piece.getPlayer())));
            pieceIcon.setFitHeight(SQUARE_SIZE);
            pieceIcon.setFitWidth(SQUARE_SIZE);
            pieceIcon.setId(idOfPiece(piece.pieceId()));
            grid.add(pieceIcon, piece.getFile(), toRowIndex(piece.getRank(), model.getRankLength()));
        });
    }

    /**
     * @return convert rank index to grid pane row index
     */
    private static int toRowIndex(int rank, int rankLength) {
        return rankLength - 1 - rank;
    }

    /**
     * @return generate id for a piece for use in JavaFX indexing
     */
    private static String idOfTile(int file, int rank) {
        return "TILE[" + file + "," + rank + "]";
    }

    /**
     * @return generate id for a piece for use in JavaFX indexing
     */
    private static String idOfPiece(String pieceId) {
        return "Piece[" + pieceId + "]";
    }
}
