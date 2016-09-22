package gui;

import core.board.Square;
import core.piece.PieceClass;
import gui.component.ConfirmBox;
import gui.component.Scoreboard;
import gui.component.TextInputBox;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
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
    @FXML private MenuItem undoButton;
    @FXML private MenuItem surrenderButton;
    @FXML private MenuItem drawGameButton;
    @FXML private MenuItem nextRoundButton;
    @FXML private MenuItem showScoreboardButton;

    public ChessController(ChessModel<P> model) {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Request player names
        initializePlayerNames();

        // Initialize grid and pieces
        initializeGrid(model.getFileLength(), model.getRankLength());
        initializePieceIcons();
        listenToTileMappingsPropertyChange();
        listenToSelectedTilePropertyChange();
        listenToMovableTilesPropertyChange();
        listenToAttackedKingPropertyChange();

        // Undo Button
        attachEventHandlerToUndoButton();
        listenToTotalMovesCountPropertyChange();

        // Next Round, Draw Game, Surrender
        attachEventHandlerToSurrenderButton();
        attachEventHandlerToDrawGameButton();
        attachEventHandlerToNextRoundButton();

        // When Game is Closed, Undo/Draw will be disabled, Next Round will be enabled
        listenToOpenGamePropertyChange();

        // ShowScoreboard Button
        attachEventHandlerToShowScoreboardButton();

        // Ask model to initialize game, this will update all the properties
        model.newRound(shouldWhiteMovesFirst());
    }

    public boolean shouldWhiteMovesFirst () {
        return ConfirmBox.display("Starting Player", "Choose which player to move first",
                model.whitePlayerNameProperty().getValue(), model.blackPlayerNameProperty().getValue());
    }

    public void initializePlayerNames() {
        String whitePlayer = TextInputBox.display("New Game", "Please enter name for White player.", "Name", "White");
        String blackPlayer = TextInputBox.display("New Game", "Please enter name for Black player.", "Name", "Black");
        if (whitePlayer != null && !whitePlayer.isEmpty()) {
            model.whitePlayerNameProperty().setValue(whitePlayer);
        }
        if (blackPlayer != null && !blackPlayer.isEmpty()) {
            model.blackPlayerNameProperty().setValue(blackPlayer);
        }
    }

    private void attachEventHandlerToShowScoreboardButton() {
        showScoreboardButton.setOnAction(event -> {
            Scoreboard.display(model.whitePlayerNameProperty().getValue(), model.blackPlayerNameProperty().getValue(),
                    model.getWhiteScore(), model.getBlackScore());
        });
    }

    private void attachEventHandlerToSurrenderButton() {
        surrenderButton.setOnAction(event -> {
            if (ConfirmBox.display("Surrender", model.getCurrentPlayerName() + ": are you sure you want to surrender?", "Yes", "No")) {
                model.surrender();
            }
        });
    }

    private void attachEventHandlerToDrawGameButton() {
        drawGameButton.setOnAction(event ->  {
            if (ConfirmBox.display("Draw Game", "Please confirm that both players agree to draw!", "Yes", "No")) {
                model.drawGame();
            }
        });
    }

    private void listenToOpenGamePropertyChange() {
        model.openGameProperty().addListener((observable, oldValue, newValue) -> {
            nextRoundButton.disableProperty().setValue(newValue);
            drawGameButton.disableProperty().setValue(!newValue);
            surrenderButton.disableProperty().setValue(!newValue);
            if (model.totalMovesProperty().get() >= 2) {
                undoButton.disableProperty().setValue(!newValue);
            }
            if (newValue) {
                board.setEffect(null);
            } else {
                board.setEffect(new ColorAdjust(0, -1, 0, 0));
            }
        });
    }

    private void attachEventHandlerToNextRoundButton() {
        nextRoundButton.setOnAction(event -> {
                model.newRound(shouldWhiteMovesFirst());
        });
    }

    private void listenToTotalMovesCountPropertyChange() {
        model.totalMovesProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 2 && model.openGameProperty().getValue()) {
                undoButton.disableProperty().setValue(false);
            } else {
                undoButton.disableProperty().setValue(true);
            }
        });
    }

    private void attachEventHandlerToUndoButton() {
        undoButton.setOnAction(event -> {
            if (ConfirmBox.display("Undo", "Please confirm that both players agree to undo last round!", "Yes", "No")) {
                model.undoLastRound();
            }
        });
    }

    private void listenToAttackedKingPropertyChange() {
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
    private void listenToMovableTilesPropertyChange() {
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
    private void listenToSelectedTilePropertyChange() {
        model.selectedTileProperty().addListener((observable, oldValue, newValue) -> {
            setColor(newValue, colorScheme.highlighted());
            resetColor(oldValue);
        });
    }

    /**
     * Subscribe to Board (Tile-Piece) mapping changes in Model and update View
     */
    private void listenToTileMappingsPropertyChange() {
        // listen to map change
        model.observableMap().addListener((MapChangeListener<Square, ChessModel.PieceId<P>>) (change -> {
            if (change.wasRemoved()) {
                ImageView icon = (ImageView) board.lookup("#" + idOfPiece(change.getValueRemoved()));
                int rowIndex = convertRowIndex(GridPane.getRowIndex(icon), model.getRankLength());
                int columnIndex = GridPane.getColumnIndex(icon);
                if (change.getKey().getFile().getCoordinate().getIndex() == columnIndex &&
                        change.getKey().getRank().getCoordinate().getIndex() == rowIndex ) {
                    // unless the icon has already been moved, in case of undo
                    inactivePieces.getChildren().add(icon); // This will also remove icon from grid
                }
            }
            if (change.wasAdded()) {
                int fileIndex = change.getKey().getFile().getCoordinate().getIndex();
                int rankIndex = change.getKey().getRank().getCoordinate().getIndex();
                // first try to find it in inactivePieces
                ImageView icon = (ImageView) inactivePieces.lookup("#" + idOfPiece(change.getValueAdded()));
                if (icon == null) {
                    // In case the icon is moving from another position on the board
                    icon = (ImageView) board.lookup("#" + idOfPiece(change.getValueAdded()));
                    // This will move icon from inactivePieces group to grid
                    GridPane.setConstraints(icon, fileIndex, convertRowIndex(rankIndex, model.getRankLength()));
                } else {
                    // This will also remove previous constraints
                    board.add(icon, fileIndex, convertRowIndex(rankIndex, model.getRankLength()));
                }
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
                board.add(tile, i, convertRowIndex(j, rankLength));
                final int rank = i, file = j;
                tile.setOnMouseClicked(event -> {
                    log.info("Clicked tile at + " + rank + ", " + file);
                    if (!model.openGameProperty().get()) {
                        return;
                    }
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
                if (!model.openGameProperty().get()) {
                    return;
                }
                model.selectSquareByPiece(pieceId);
            });
            inactivePieces.getChildren().add(icon);
        });
    }

    /**
     * @return convert rank index to grid pane row index
     */
    private static int convertRowIndex(int rank, int rankLength) {
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
