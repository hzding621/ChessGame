package gui;

import core.board.Square;
import core.piece.PieceClass;
import gui.component.ConfirmBox;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class contains the view for the chess board
 */
public class ChessController<P extends PieceClass> implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(ChessController.class);

    private static final int SQUARE_SIZE = 75;
    private final ColorScheme colorScheme = ColorScheme.STANDARD;
    private final Group inactivePieces = new Group();
    private final ChessModel<P> model;

    // Initialized in FXMLLoader
    @FXML private BorderPane layout;
    @FXML private GridPane board;
    @FXML private Menu gameMenu;
    @FXML private MenuItem undoButton;
    @FXML private MenuItem newGameButton;


    public ChessController(ChessModel<P> model) {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(model.getFileLength(), model.getRankLength());
        initializePieceIcons();
        listenToBoardChanges();
        listenToSelectedTileChanges();
        listenToMovableTilesChanges();
        listenToAttackedKingChange();

        // Undo Button
        attachEventHandlerForUndoButton();
        listenToTotalMovesCount();

        // New Game Button
        attachEventHandlerForNewGameButton();

        // Ask model to populate all pieces
        model.pullPiecesLocationsUpdate();
    }

    private void attachEventHandlerForNewGameButton() {
        newGameButton.setOnAction(event -> {
            if (ConfirmBox.display("New Game", "Please confirm that both players agree to restart!")) {
                model.newGame();
            }
        });
    }

    private void listenToTotalMovesCount() {
        model.totalMovesProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 2) {
                undoButton.disableProperty().setValue(false);
            } else {
                undoButton.disableProperty().setValue(true);
            }
        });
    }

    private void attachEventHandlerForUndoButton() {
        undoButton.setOnAction(event -> {
            if (ConfirmBox.display("Undo", "Please confirm that both players agree to undo last round!")) {
                model.undoLastRound();
            }
        });
    }

    private void listenToAttackedKingChange() {
        model.attackedKingProperty().addListener((observable, oldValue, newValue) -> {
            InnerShadow shade = new InnerShadow(30, Color.RED);
            if (oldValue != null) {
                int file = oldValue.getFile().getCoordinate().getIndex();
                int rank = oldValue.getRank().getCoordinate().getIndex();
                Rectangle tile = (Rectangle) board.lookup("#" + idOfTile(file, rank));
                tile.setEffect(null);
            }
            if (newValue != null) {
                int file = newValue.getFile().getCoordinate().getIndex();
                int rank = newValue.getRank().getCoordinate().getIndex();
                Rectangle tile = (Rectangle) board.lookup("#" + idOfTile(file, rank));
                tile.setEffect(shade);
            }
        });
    }

    private void setColor(Square square, Color color) {
        if (square != null) {
            int file = square.getFile().getCoordinate().getIndex();
            int rank = square.getRank().getCoordinate().getIndex();
            Rectangle newTile = (Rectangle) board.lookup("#" + idOfTile(file, rank));
            newTile.setFill(color);
        }
    }

    private void resetColor(Square square) {
        if (square != null) {
            int file = square.getFile().getCoordinate().getIndex();
            int rank = square.getRank().getCoordinate().getIndex();
            Rectangle tile = (Rectangle) board.lookup("#" + idOfTile(file, rank));
            tile.setFill(getDefaultColor(file, rank));
        }
    }

    /**
     * Subscribe to the changes to highlighted movable tiles in Model and update View
     */
    private void listenToMovableTilesChanges() {
        model.movableTilesProperty().addListener((ListChangeListener<Square>) (c -> {
            c.next();
            c.getRemoved().forEach(this::resetColor);
            c.getAddedSubList().forEach(o ->  {
                int file = o.getFile().getCoordinate().getIndex();
                int rank = o.getRank().getCoordinate().getIndex();
                Rectangle tile = (Rectangle) board.lookup("#" + idOfTile(file, rank));
                tile.setFill(getMovableColor(file, rank));
            });
        }));
    }

    /**
     * Subscribe to the changes of selected tile in Model and update View
     */
    private void listenToSelectedTileChanges() {
        model.selectedTileProperty().addListener((observable, oldValue, newValue) -> {
            setColor(newValue, colorScheme.highlighted());
            resetColor(oldValue);
        });
    }

    /**
     * Subscribe to Board (Tile-Piece) mapping changes in Model and update View
     */
    private void listenToBoardChanges() {
        // listen to map change
        model.observableMap().addListener((MapChangeListener<Square, ChessModel.PieceId<P>>) (change -> {
            if (change.wasRemoved()) {
                ImageView icon = (ImageView) board.lookup("#" + idOfPiece(change.getValueRemoved()));
                inactivePieces.getChildren().add(icon); // This will also remove icon from grid
            }
            if (change.wasAdded()) {
                int fileIndex = change.getKey().getFile().getCoordinate().getIndex();
                int rankIndex = change.getKey().getRank().getCoordinate().getIndex();
                board.add(inactivePieces.lookup("#" + idOfPiece(change.getValueAdded())), fileIndex,
                        toRowIndex(rankIndex, model.getRankLength()));  // This will also remove icon from inactive group
            }
        }));
    }

    /**
     * Initialize Grid with certain file length and rank length, and attach EventHandlers to each tiles
     */
    private void initializeGrid(int fileLength, int rankLength) {

        board.setAlignment(Pos.CENTER);
        board.setPadding(new Insets(20, 20, 20, 20));

        // Create Rows and Columns
        for (int i = 0; i < fileLength; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(SQUARE_SIZE);
            column.setHalignment(HPos.CENTER);
            board.getColumnConstraints().add(column);
        }
        for (int j = 0; j < rankLength; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(SQUARE_SIZE);
            row.setValignment(VPos.CENTER);
            board.getRowConstraints().add(row);
        }

        // Create 8x8 tiles
        for (int i = 0; i < fileLength; i++) {
            for (int j = 0; j < rankLength; j ++) {
                Rectangle tile = new Rectangle();
                tile.setWidth(SQUARE_SIZE);
                tile.setHeight(SQUARE_SIZE);
                tile.setFill(getDefaultColor(i, j));
                tile.setId(idOfTile(i, j));
                board.add(tile, i, toRowIndex(j, rankLength));
                final int rank = i, file = j;
                tile.setOnMouseClicked(event -> {
                    log.info("Clicked tile at + " + rank + ", " + file);
                    model.selectSquare(rank, file);
                });
            }
        }
    }

    /**
     * Instantiate piece images for each piece and add them to IconGroup
     */
    private void initializePieceIcons() {
        model.piecesConfiguration().forEach(pieceId -> {
            ImageView icon = new ImageView();
            icon.setImage(new Image(pieceId.getShortId()+".png"));
            icon.setFitHeight(SQUARE_SIZE);
            icon.setFitWidth(SQUARE_SIZE);
            icon.setId(idOfPiece(pieceId));
            icon.setOnMouseClicked(event -> {
                log.info("Clicked piece " + pieceId);
                model.selectSquareByPiece(pieceId);
            });
            inactivePieces.getChildren().add(icon);
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
