package chessgame.board;

import chessgame.piece.*;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends RectangularBoard<ChessPieceType, Piece<ChessPieceType>> {

    private final Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
    private final SquareCell.Builder cellBuilder =
            new SquareCell.Builder(coordinateBuilder, coordinateBuilder);

    public ChessBoard(PieceSet<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceSet) {
        super(pieceSet);
    }

    @Override
    public GridCellFactory<SquareCell, SquareDirection> getGridCellFactory() {
        return cellBuilder;
    }
}
