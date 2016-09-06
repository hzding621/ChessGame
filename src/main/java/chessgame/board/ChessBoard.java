package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.piece.*;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends RectangularBoard<StandardPieces> {

    private final Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
    private final Square.Builder cellBuilder =
            new Square.Builder(coordinateBuilder, coordinateBuilder);

    public ChessBoard(GameSetting<Square, StandardPieces> gameSetting) {
        super(gameSetting);
    }

    @Override
    public GridCellFactory<Square, TwoDimension> getGridCellFactory() {
        return cellBuilder;
    }
}
