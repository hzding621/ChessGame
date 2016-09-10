package chessgame.piece;

import chessgame.board.Coordinate;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.ConfigurableGameSetting;
import chessgame.game.PieceInformation;
import chessgame.game.RuntimeInformation;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

/**
 * Contains tests for Pawn
 */
@RunWith(MockitoJUnitRunner.class)
public final class PawnTest {

    private Square.Builder builder;
    private RectangularBoard.Instance<StandardPieces> testBoard;
    private Pawn<Square, StandardPieces, TwoDimension, RectangularBoard.Instance<StandardPieces>> rule;
    @Mock private PieceInformation<Square, StandardPieces> pieceInformation;
    @Mock private RuntimeInformation<Square, StandardPieces> runtimeInformation;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        rule = new Pawn<>(runtimeInformation);
    }

    @Test
    public void testAttacking() {
        // white pawn at E2
        // white king at E1
        // black king at E8

        testBoard = RectangularBoard.Instance.create(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.PAWN, Player.WHITE, "E", "2")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .piece(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );

        Mockito.when(pieceInformation.getPieceMoveCount(Matchers.any())).thenReturn(0);
        Mockito.when(pieceInformation.locateKing(Player.WHITE)).thenReturn(builder.at("E", "1"));
        Mockito.when(pieceInformation.locateKing(Player.BLACK)).thenReturn(builder.at("E", "8"));
        Mockito.when(runtimeInformation.getPieceInformation()).thenReturn(pieceInformation);

        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("D", "4"), Player.WHITE);
        Assert.assertEquals(2, attacked.size());
    }
}