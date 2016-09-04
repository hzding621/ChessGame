package chessgame.board;

import chessgame.piece.*;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends RectangularBoard<ChessPieceType, Piece<ChessPieceType>>
        implements GridBoard<SquareCell, SquareDirection, ChessPieceType, Piece<ChessPieceType>> {

    private final Coordinate.Factory coordinateFactory = new Coordinate.Factory(8);
    private final SquareCell.Factory cellFactory =
            new SquareCell.Factory(coordinateFactory, coordinateFactory);

    protected ChessBoard(PieceSet<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceSet) {
        super(pieceSet);
    }

    @Override
    public GridCellFactory<SquareCell, SquareDirection> getGridCellFactory() {
        return cellFactory;
    }
}
