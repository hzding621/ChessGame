package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard<P extends PieceClass> implements
        MutableBoard<Square, P, ChessBoard<P>>,
        ChessBoardViewer<P>,
        Previewer<Square, P, ChessBoardViewer<P>, ChessBoard<P>> {

    private ChessBoardImpl<P> delegate;
    private ChessBoard(ChessBoardImpl<P> delegate) {
        this.delegate = delegate;
    }

    public static <P extends PieceClass> ChessBoard<P> create(GameSetting.GridGame<Square, P> gameSetting) {
        final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
        final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
        return new ChessBoard<P>(new ChessBoardImpl<P>(gameSetting.constructPiecesByStartingPosition(),
                new Square.Builder(fileBuilder, rankBuilder)));
    }

    @Override
    public Optional<Piece<P>> getPiece(Square cell) {
        return delegate.getPiece(cell);
    }

    @Override
    public Collection<Square> getPiecesOfTypeForPlayer(P type, Player player) {
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
    public Piece<P> clearPiece(Square position) {
        return delegate.clearPiece(position);
    }

    @Override
    public Piece<P> movePiece(Square source, Square target) {
        return delegate.movePiece(source, target);
    }

    @Override
    public void addPiece(Square position, Piece<P> piece) {
        delegate.addPiece(position, piece);
    }

    @Override
    public TransitionResult<Square, P> apply(BoardTransition<Square, P, ChessBoard<P>> boardTransition) {
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
    public Optional<Square> moveSteps(Square startCell, TwoDimension direction, int steps, Distance distance) {
        return delegate.moveSteps(startCell, direction, steps, distance);
    }

    @Override
    public List<Square> furthestReach(Square startCell, TwoDimension direction, Distance distance, boolean startInclusive, boolean meetInclusive) {
        return delegate.furthestReach(startCell, direction, distance, startInclusive, meetInclusive);
    }

    @Override
    public Optional<Square> firstOccupant(Square startCell, TwoDimension direction, Distance distance) {
        return delegate.firstOccupant(startCell, direction, distance);
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
    public Collection<Square> getAllPositions() {
        return delegate.getAllPositions();
    }

    @Override
    public ChessBoardViewer<P> preview(BoardTransition<Square, P, ChessBoard<P>> transition) {
        ChessBoard<P> newBoard = new ChessBoard<P>(new ChessBoardImpl(delegate.occupants, delegate.cellBuilder));
        newBoard.apply(transition);
        return newBoard;
    }

    private static final class ChessBoardImpl<P extends PieceClass> extends RectangularBoard<P, GridViewer<Square, TwoDimension, P>, ChessBoardImpl<P>> {

        private ChessBoardImpl(Map<Square, Piece<P>> occupants, Square.Builder cellBuilder) {
            super(occupants, cellBuilder);
        }

        @Override
        public TransitionResult<Square, P> apply(BoardTransition<Square, P, ChessBoardImpl<P>> boardTransition) {
            return boardTransition.apply(this);
        }

        @Override
        public GridViewer<Square, TwoDimension, P> preview(BoardTransition<Square, P, ChessBoardImpl<P>> transition) {
            ChessBoardImpl<P> chessBoard = new ChessBoardImpl<P>(occupants, cellBuilder);
            chessBoard.apply(transition);
            return chessBoard;
        }
    }
}
