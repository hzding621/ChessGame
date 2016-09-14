package core.piece;

import core.board.ChessBoard;
import core.board.Coordinate;
import core.board.Square;
import core.board.TwoDimension;
import core.game.ConfigurableGameSetting;
import core.player.Player;
import core.rule.LatentAttack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Queen
 */
public class QueenTest {

    private Square.Builder builder;
    private ChessBoard<StandardPieces> testBoard;
    private Queen<Square, StandardPieces, TwoDimension, ChessBoard<StandardPieces>> rule;

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

        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(3, 3)
                .set(StandardPieces.QUEEN, Player.WHITE, "B", "2")
                .set(StandardPieces.KING, Player.WHITE, "A", "1")
                .set(StandardPieces.KING, Player.BLACK, "A", "3")
                .build()
        );

        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("B", "2"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());
    }

    @Test
    public void testLatentAttacking() {

        // K p Q p k

        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(5, 1)
                .set(StandardPieces.KING, Player.BLACK, "A", "1")
                .set(StandardPieces.PAWN, Player.BLACK, "B", "1")
                .set(StandardPieces.QUEEN, Player.WHITE, "C", "1")
                .set(StandardPieces.PAWN, Player.WHITE, "D", "1")
                .set(StandardPieces.KING, Player.WHITE, "E", "1")
                .build()
        );
        Collection<LatentAttack<Square>> attacked =
                rule.latentAttacking(testBoard, builder.at("C", "1"), Player.WHITE);
        Assert.assertEquals(1, attacked.size());
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("A", "1"))
                && at.getBlocker().equals(builder.at("B", "1"))));
    }

    @Test
    public void testAttackCompositeVersion() {
        // white Queen at center (B2) attacking all eight locations
        // white king at A1
        // black king at A3

        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(3, 3)
                .set(StandardPieces.QUEEN, Player.WHITE, "B", "2")
                .set(StandardPieces.KING, Player.WHITE, "A", "1")
                .set(StandardPieces.KING, Player.BLACK, "A", "3")
                .build()
        );

        Queen.Composite<Square, StandardPieces, TwoDimension, ChessBoard<StandardPieces>>
                rule = new Queen.Composite<>();
        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("B", "2"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());
    }
}
