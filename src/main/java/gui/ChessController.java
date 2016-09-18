package gui;

import core.board.Square;
import core.piece.Piece;
import core.piece.PieceClass;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class contains the view for the chess board
 */
public class ChessController<P extends PieceClass> implements Initializable {

    private static final int SQUARE_SIZE = 60;
    private final ChessModel<P> model;

    private final Group iconBank = new Group();

    @FXML
    GridPane layout;

    public ChessController(ChessModel<P> model) {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(model.getFileLength(), model.getRankLength(), ColorScheme.STANDARD);

        // listen to map change
        model.getObservableMap().addListener((MapChangeListener<Square, Piece<P>>) (change -> {
            if (change.wasRemoved()) {
                GridPane.clearConstraints(iconBank.lookup("#" + idOfPiece(change.getValueRemoved().toString())));
            }
            if (change.wasAdded()) {
                int fileIndex = change.getKey().getFile().getCoordinate().getIndex();
                int rankIndex = change.getKey().getRank().getCoordinate().getIndex();
                String id = idOfPiece(change.getValueAdded().toString());
                layout.add(iconBank.lookup("#" + id), fileIndex, toRowIndex(rankIndex, model.getRankLength()));
            }
        }));

        // Ask model to populate all pieces
        model.refreshAllPieces();
    }

    /**
     * Initialize Grid with certain file length and rank length and a color scheme
     */
    private void initializeGrid(int fileLength, int rankLength, ColorScheme colorScheme) {

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40, 40, 40, 40));

        // Create Rows and Columns
        for (int i = 0; i < fileLength; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(SQUARE_SIZE);
            column.setHalignment(HPos.CENTER);
            layout.getColumnConstraints().add(column);
        }
        for (int j = 0; j < rankLength; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(SQUARE_SIZE);
            row.setValignment(VPos.CENTER);
            layout.getRowConstraints().add(row);
        }

        // Create 8x8 tiles
        for (int i = 0; i < fileLength; i++) {
            for (int j = 0; j < rankLength; j ++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(SQUARE_SIZE);
                rectangle.setHeight(SQUARE_SIZE);
                rectangle.setFill((i + j) % 2 == 0 ? colorScheme.dark() : colorScheme.light());
                rectangle.setId(idOfTile(i, j));
                layout.add(rectangle, i, toRowIndex(j, rankLength));
            }
        }

        model.piecesConfiguration().forEach(pieceId -> {
            ImageView icon = new ImageView();
            icon.setImage(new Image(pieceId.getShortId()+".png"));
            icon.setFitHeight(SQUARE_SIZE);
            icon.setFitWidth(SQUARE_SIZE);
            icon.setId(idOfPiece(pieceId.toString()));
            iconBank.getChildren().add(icon);
        });
    }

    /**
     * Add piece as  Rectangles (JavaFX resource) to the grid pane
     */
    private void refreshAllPieces() {
        model.refreshAllPieces();
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
