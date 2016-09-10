package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.ConfigurableGameSetting;
import chessgame.player.Player;
import chessgame.rule.LatentAttack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Queen
 */
public class QueenTest {

    private Square.Builder builder;
    private RectangularBoard.Instance<StandardPieces> testBoard;
    private Queen<Square, StandardPieces, TwoDimension, RectangularBoard.Instance<StandardPieces>> rule;

    @Before
    public void instantiateTestPieceSet() {
        // Create a 5x5 board for ease of testing

        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(3);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        rule = new Queen<>();
    }

    @Test
    public void testAttacking() {
        // white Queen at center (B2) attacking all eight locations
        // white king at A1
        // black king at A3

        testBoard = RectangularBoard.Instance.create(ConfigurableGameSetting.builder(3, 3)
                .piece(StandardPieces.QUEEN, Player.WHITE, "B", "2")
                .piece(StandardPieces.KING, Player.WHITE, "A", "1")
                .piece(StandardPieces.KING, Player.BLACK, "A", "3")
                .build()
        );

        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("B", "2"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());
    }

    @Test
    public void testLatentAttacking() {

        // K p Q p k

        testBoard = RectangularBoard.Instance.create(ConfigurableGameSetting.builder(5, 1)
                .piece(StandardPieces.KING, Player.BLACK, "A", "1")
                .piece(StandardPieces.PAWN, Player.BLACK, "B", "1")
                .piece(StandardPieces.QUEEN, Player.WHITE, "C", "1")
                .piece(StandardPieces.PAWN, Player.WHITE, "D", "1")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .build()
        );
        Collection<LatentAttack<Square>> attacked =
                rule.latentAttacking(testBoard, builder.at("C", "1"), Player.WHITE);
        Assert.assertEquals(1, attacked.size());
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("A", "1"))
                && at.getBlocker().equals(builder.at("B", "1"))));
    }
}
