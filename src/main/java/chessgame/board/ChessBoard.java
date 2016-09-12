package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard<P extends PieceClass>
        extends RectangularBoard<P, GridViewer<Square, TwoDimension, P>, ChessBoard<P>>
        implements Previewer<Square, P, ChessBoardViewer<P>, ChessBoard<P>>, ChessBoardViewer<P> {

    private ChessBoard(Map<Square, Piece<P>> occupants, Square.Builder cellBuilder) {
        super(occupants, cellBuilder);
    }

    public static <P extends PieceClass> ChessBoard<P> create(GameSetting.GridGame<Square, P> gameSetting) {
        final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
        final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
        return new ChessBoard<P>(gameSetting.constructPiecesByStartingPosition(),
                new Square.Builder(fileBuilder, rankBuilder));
    }

    @Override
    public TransitionResult<Square, P> apply(BoardTransition<Square, P, ChessBoard<P>> boardTransition) {
        return boardTransition.apply(this);
    }

    @Override
    public void preview(BoardTransition<Square, P, ChessBoard<P>> transition, Consumer<ChessBoardViewer<P>> callback) {
        ChessBoard<P> chessBoard = new ChessBoard<P>(occupants, cellBuilder); // Creates a copy
        chessBoard.apply(transition);
        callback.accept(chessBoard);
    }

    @Override
    public <T> T preview(BoardTransition<Square, P, ChessBoard<P>> transition, Function<ChessBoardViewer<P>, T> callback) {
        ChessBoard<P> chessBoard = new ChessBoard<P>(occupants, cellBuilder); // Creates a copy
        chessBoard.apply(transition);
        return callback.apply(chessBoard);
    }
}
