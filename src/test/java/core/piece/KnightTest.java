package core.piece;

import core.board.ChessBoard;
import core.board.Coordinate;
import core.board.Square;
import core.board.TwoDimension;
import core.game.ConfigurableGameSetting;
import core.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Knight
 */
public final class KnightTest {

    private Square.Builder builder;
    private ChessBoard<StandardPieces> testBoard;
    private Knight<Square, StandardPieces, TwoDimension, ChessBoard<StandardPieces>> rule;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        rule = new Knight<>();
    }

    @Test
    public void testAttacking() {
        // white knight at D4
        // white king at E1
        // black king at E8
        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                .set(StandardPieces.KNIGHT, Player.WHITE, "D", "4")
                .set(StandardPieces.KING, Player.WHITE, "E", "1")
                .set(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("D", "4"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());
    }

    @Test
    public void testAttackingAtCorner() {
        // white knight at H1
        // white king at E1
        // black king at E8
        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                .set(StandardPieces.KNIGHT, Player.WHITE, "H", "1")
                .set(StandardPieces.KING, Player.WHITE, "E", "1")
                .set(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("H", "1"), Player.WHITE);
        Assert.assertEquals(2, attacked.size());
    }

    @Test
    public void testGetBlockingPositionWhenAttacking() {
        // white knight at F6
        // white king at E1
        // black king at E8
        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                .set(StandardPieces.KNIGHT, Player.WHITE, "F", "6")
                .set(StandardPieces.KING, Player.WHITE, "E", "1")
                .set(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<Square> blockingPositionsWhenAttacking =
                rule.attackBlockingPositions(
                        testBoard, builder.at("F", "6"), builder.at("E", "8"), Player.WHITE);
        Assert.assertEquals(1, blockingPositionsWhenAttacking.size());
        Assert.assertTrue(blockingPositionsWhenAttacking.contains(builder.at("F", "6")));
    }
}
