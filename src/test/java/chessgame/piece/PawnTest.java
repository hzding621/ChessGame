package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.game.ConfigurableGameSetting;
import chessgame.game.PieceInformation;
import chessgame.move.MoveResult;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.mockito.Matchers.any;

/**
 * Contains tests for Pawn
 */
@RunWith(MockitoJUnitRunner.class)
public final class PawnTest {

    private Square.Builder builder;
    private RectangularBoard<StandardPieces> testBoard;
    @Mock private PieceInformation<Square, StandardPieces> pieceInformation;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
    }

    @Test
    public void testAttacking() {
        // white pawn at E2
        // white king at E1
        // black king at E8

        testBoard = new RectangularBoard<>(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.PAWN, Player.WHITE, "E", "2")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .piece(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );

        Mockito.when(pieceInformation.getPieceMoveCount(any(Piece.class))).thenReturn(0);
        Mockito.when(pieceInformation.locateKing(Player.WHITE)).thenReturn(builder.at("E", "1"));
        Mockito.when(pieceInformation.locateKing(Player.BLACK)).thenReturn(builder.at("E", "8"));

        Collection<Square> attacked =
                new Pawn.PawnRule<>(testBoard, pieceInformation).attacking(builder.at("D", "4"), Player.WHITE);
        Assert.assertEquals(2, attacked.size());
    }
}