package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.piece.*;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends RectangularBoard<ChessPieceType, Piece<ChessPieceType>> {

    private final Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
    private final SquareCell.Builder cellBuilder =
            new SquareCell.Builder(coordinateBuilder, coordinateBuilder);

    public ChessBoard(GameSetting<SquareCell, ChessPieceType, Piece<ChessPieceType>> gameSetting) {
        super(gameSetting);
    }

    @Override
    public GridCellFactory<SquareCell, SquareDirection> getGridCellFactory() {
        return cellBuilder;
    }
}
