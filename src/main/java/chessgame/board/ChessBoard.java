package chessgame.board;

import chessgame.game.StandardSetting;
import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard implements
        MutableBoard<Square, StandardPieces, ChessBoard>,
        ChessBoardViewer,
        Previewer<Square, StandardPieces, ChessBoardViewer, ChessBoard>
    {


    private ChessBoardImpl delegate;
    private ChessBoard(ChessBoardImpl delegate) {
        this.delegate = delegate;
    }

    public static ChessBoard create(StandardSetting gameSetting) {
        final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
        final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
        return new ChessBoard(new ChessBoardImpl(gameSetting.constructPiecesByStartingPosition(),
                new Square.Builder(fileBuilder, rankBuilder)));
    }

    @Override
    public Optional<Piece<StandardPieces>> getPiece(Square cell) {
        return delegate.getPiece(cell);
    }

    @Override
    public Collection<Square> getPiecesOfTypeForPlayer(StandardPieces type, Player player) {
        return delegate.getPiecesOfTypeForPlayer(type, player);
    }

    @Override
    public Collection<Square> getPiecesForPlayer(Player player) {
        return delegate.getPiecesForPlayer(player);
    }

    @Override
    public boolean isOccupied(Square cell) {
        return delegate.isOccupied(cell);
    }

    @Override
    public boolean isEnemy(Square cell, Player player) {
        return delegate.isEnemy(cell, player);
    }

    @Override
    public Piece<StandardPieces> clearPiece(Square position) {
        return delegate.clearPiece(position);
    }

    @Override
    public Piece<StandardPieces> movePiece(Square source, Square target) {
        return delegate.movePiece(source, target);
    }

    @Override
    public void addPiece(Square position, Piece<StandardPieces> piece) {
        delegate.addPiece(position, piece);
    }

    @Override
    public TransitionResult<Square, StandardPieces> apply(BoardTransition<Square, StandardPieces, ChessBoard> boardTransition) {
        return boardTransition.apply(this);
    }

    @Override
    public Collection<TwoDimension> getAllDirections() {
        return delegate.getAllDirections();
    }

    @Override
    public Collection<TwoDimension> getOrthogonalDirections() {
        return delegate.getOrthogonalDirections();
    }

    @Override
    public Collection<TwoDimension> getDiagonalDirections() {
        return delegate.getDiagonalDirections();
    }

    @Override
    public Optional<Square> moveSteps(Square startCell, TwoDimension direction, int steps, Projection projection) {
        return delegate.moveSteps(startCell, direction, steps, projection);
    }

    @Override
    public List<Square> furthestReach(Square startCell, TwoDimension direction, Projection projection, boolean startInclusive, boolean meetInclusive) {
        return delegate.furthestReach(startCell, direction, projection, startInclusive, meetInclusive);
    }

    @Override
    public Optional<Square> firstOccupant(Square startCell, TwoDimension direction, Projection projection) {
        return delegate.firstOccupant(startCell, direction, projection);
    }

    @Override
    public Optional<Square> moveForward(Square startCell, Player player) {
        return delegate.moveForward(startCell, player);
    }

    @Override
    public Collection<Square> attackPawnStyle(Square startCell, Player player) {
        return delegate.attackPawnStyle(startCell, player);
    }

    @Override
    public TwoDimension findDirection(Square startCell, Square endCell) {
        return delegate.findDirection(startCell, endCell);
    }

    @Override
    public GridCellBuilder<Square, TwoDimension> getGridCellFactory() {
        return delegate.getGridCellFactory();
    }

    @Override
    public ChessBoardViewer preview(BoardTransition<Square, StandardPieces, ChessBoard> transition) {
        ChessBoard newBoard = new ChessBoard(new ChessBoardImpl(delegate.occupants, delegate.cellBuilder));
        newBoard.apply(transition);
        return newBoard;
    }

    private static final class ChessBoardImpl extends RectangularBoard<StandardPieces, GridViewer<Square, TwoDimension, StandardPieces>, ChessBoardImpl> {

        private ChessBoardImpl(Map<Square, Piece<StandardPieces>> occupants, Square.Builder cellBuilder) {
            super(occupants, cellBuilder);
        }

        @Override
        public TransitionResult<Square, StandardPieces> apply(BoardTransition<Square, StandardPieces, ChessBoardImpl> boardTransition) {
            return boardTransition.apply(this);
        }

        @Override
        public GridViewer<Square, TwoDimension, StandardPieces> preview(BoardTransition<Square, StandardPieces, ChessBoardImpl> transition) {
            ChessBoardImpl chessBoard = new ChessBoardImpl(occupants, cellBuilder);
            chessBoard.apply(transition);
            return chessBoard;
        }
    }
}
