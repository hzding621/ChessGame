package core.board;

import core.game.GameSetting;
import core.move.BoardTransition;
import core.piece.Piece;
import core.piece.PieceClass;

import java.util.Map;
import java.util.function.Function;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard<P extends PieceClass> extends RectangularBoard<P>
        implements Previewable<Square, P, ChessBoardViewer<P>, ChessBoard<P>>, ChessBoardViewer<P> {

    private ChessBoard(Map<Square, Piece<P>> occupants, Square.Builder tileBuilder) {
        super(occupants, tileBuilder);
    }

    public static <P extends PieceClass> ChessBoard<P> create(GameSetting.GridGame<Square, P> gameSetting) {
        final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
        final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
        return new ChessBoard<>(gameSetting.constructPiecesByStartingPosition(),
                new Square.Builder(fileBuilder, rankBuilder));
    }

    @Override
    public <T> T preview(BoardTransition<Square, P, ChessBoard<P>> transition, Function<ChessBoardViewer<P>, T> callback) {
        BoardTransition<Square, P, ChessBoard<P>> reverser = transition.transition(this).getReverseMove().getTransition();
        T result = callback.apply(this);
        reverser.transition(this);
        return result;
    }
}
