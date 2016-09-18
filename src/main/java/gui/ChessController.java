package gui;

import core.board.Square;
import core.piece.PieceClass;
import javafx.collections.ListChangeListener;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class contains the view for the chess board
 */
public class ChessController<P extends PieceClass> implements Initializable {

    private static final int SQUARE_SIZE = 60;
    private final ColorScheme colorScheme = ColorScheme.STANDARD;
    private final Group iconBank = new Group();
    private final ChessModel<P> model;

    @FXML
    private GridPane layout;  // Initialized in FXMLLoader

    public ChessController(ChessModel<P> model) {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(model.getFileLength(), model.getRankLength());

        // listen to map change
        model.getObservableMap().addListener((MapChangeListener<Square, ChessModel.PieceId<P>>) (change -> {
            if (change.wasRemoved()) {
                GridPane.clearConstraints(iconBank.lookup("#" + idOfPiece(change.getValueRemoved())));
            }
            if (change.wasAdded()) {
                int fileIndex = change.getKey().getFile().getCoordinate().getIndex();
                int rankIndex = change.getKey().getRank().getCoordinate().getIndex();
                layout.add(iconBank.lookup("#" + idOfPiece(change.getValueAdded())), fileIndex,
                        toRowIndex(rankIndex, model.getRankLength()));
            }
        }));

        // listen to selectedTile changes
        model.getSelectedTile().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                int newFile = newValue.getFile().getCoordinate().getIndex();
                int newRank = newValue.getRank().getCoordinate().getIndex();
                Rectangle newTile = (Rectangle) layout.lookup("#" + idOfTile(newFile, newRank));
                newTile.setFill(colorScheme.highlighted());
            }

            if (oldValue != null) {
                int oldFile = oldValue.getFile().getCoordinate().getIndex();
                int oldRank = oldValue.getRank().getCoordinate().getIndex();
                Rectangle oldTile = (Rectangle) layout.lookup("#" + idOfTile(oldFile, oldRank));
                oldTile.setFill(getDefaultColor(oldFile, oldRank));
            }
        });

        // listen to movableTiles changes
        model.getMovableTiles().addListener((ListChangeListener<Square>) (c -> {
            c.next();
            c.getRemoved().forEach(o ->  {
                int file = o.getFile().getCoordinate().getIndex();
                int rank = o.getRank().getCoordinate().getIndex();
                Rectangle tile = (Rectangle) layout.lookup("#" + idOfTile(file, rank));
                tile.setFill(getDefaultColor(file, rank));
            });

            c.getAddedSubList().forEach(o ->  {
                int file = o.getFile().getCoordinate().getIndex();
                int rank = o.getRank().getCoordinate().getIndex();
                Rectangle tile = (Rectangle) layout.lookup("#" + idOfTile(file, rank));
                tile.setFill(getMovableColor(file, rank));
            });
        }));
        // Ask model to populate all pieces
        model.refreshAllPieces();
    }

    /**
     * Initialize Grid with certain file length and rank length and a color scheme
     */
    private void initializeGrid(int fileLength, int rankLength) {

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
                Rectangle tile = new Rectangle();
                tile.setWidth(SQUARE_SIZE);
                tile.setHeight(SQUARE_SIZE);
                tile.setFill(getDefaultColor(i, j));
                tile.setId(idOfTile(i, j));
                layout.add(tile, i, toRowIndex(j, rankLength));
                final int rank = i, file = j;
                tile.setOnMouseClicked(event -> model.setSelectedSquare(rank, file));
            }
        }

        model.piecesConfiguration().forEach(pieceId -> {
            ImageView icon = new ImageView();
            icon.setImage(new Image(pieceId.getShortId()+".png"));
            icon.setFitHeight(SQUARE_SIZE);
            icon.setFitWidth(SQUARE_SIZE);
            icon.setId(idOfPiece(pieceId));
            icon.setOnMouseClicked(event -> model.setSelectedTile(pieceId));
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
    private static <P extends PieceClass> String idOfPiece(ChessModel.PieceId<P> pieceId) {
        return "Piece[" + pieceId + "]";
    }

    private Color getDefaultColor(int file, int rank) {
        return (file + rank) % 2 == 1 ? colorScheme.dark() : colorScheme.light();
    }

    private Color getMovableColor(int file, int rank) {
        return (file + rank) % 2 == 1 ? colorScheme.movableDark() : colorScheme.movableLight();
    }
}
