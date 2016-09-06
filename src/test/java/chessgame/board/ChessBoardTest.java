package chessgame.board;

import chessgame.game.StandardSetting;
import chessgame.player.Player;
import chessgame.rule.Pin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utility.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Contains tests for ChessBoard
 */
public final class ChessBoardTest {

    private Square.Builder builder;
    private ChessBoard testBoard;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        testBoard = new ChessBoard(new StandardSetting());
    }

    @Test
    public void testFurthestReach() {
        // (B2) [B3 -> B7]
        List<Square> path = testBoard.furthestReach(builder.at("B", "2"), TwoDimension.NORTH);
        Assert.assertEquals(path.size(), 5);
        Assert.assertEquals(path.get(0), builder.at("B", "3"));
        Assert.assertEquals(CollectionUtils.last(path).get(), builder.at("B", "7"));

        // (B2) [C3 -> G7]
        path = testBoard.furthestReach(builder.at("B", "2"), TwoDimension.NORTHEAST);
        Assert.assertEquals(path.size(), 5);
        Assert.assertEquals(path.get(0), builder.at("C", "3"));
        Assert.assertEquals(CollectionUtils.last(path).get(), builder.at("G", "7"));

        // (B2) [B1]
        path = testBoard.furthestReach(builder.at("B", "2"), TwoDimension.SOUTH);
        Assert.assertEquals(path.size(), 1);
        Assert.assertEquals(path.get(0), builder.at("B", "1"));
    }

    @Test
    public void testFindPin() {

        // Move white Queen to E4
        // - pinning black Pawn at E7, black King at E8,
        // - pinning black Pawn at B7, black Rook at A8
        testBoard.movePiece(builder.at("D", "1"), builder.at("E", "4"));

        Optional<Pin<Square>> pin = testBoard.findPin(builder.at("E", "4"), TwoDimension.NORTH, Player.WHITE);
        Assert.assertTrue(pin.isPresent());
        Assert.assertEquals(pin.get().getAttacker(), builder.at("E", "4"));
        Assert.assertEquals(pin.get().getProtector(), builder.at("E", "7"));
        Assert.assertEquals(pin.get().getHided(), builder.at("E", "8"));

        pin = testBoard.findPin(builder.at("E", "4"), TwoDimension.NORTHWEST, Player.WHITE);
        Assert.assertTrue(pin.isPresent());
        Assert.assertEquals(pin.get().getAttacker(), builder.at("E", "4"));
        Assert.assertEquals(pin.get().getProtector(), builder.at("B", "7"));
        Assert.assertEquals(pin.get().getHided(), builder.at("A", "8"));

        // Does not consider my own pieces
        pin = testBoard.findPin(builder.at("E", "4"), TwoDimension.SOUTH, Player.WHITE);
        Assert.assertFalse(pin.isPresent());
    }

    @Test
    public void testAttackPawnStyle() {
        Collection<Square> attackedPositions = testBoard.attackPawnStyle(builder.at("E", "2"), Player.WHITE);
        Assert.assertEquals(attackedPositions.size(), 2);
        Assert.assertTrue(attackedPositions.contains(builder.at("D", "3")));
        Assert.assertTrue(attackedPositions.contains(builder.at("F", "3")));

        attackedPositions = testBoard.attackPawnStyle(builder.at("E", "7"), Player.BLACK);
        Assert.assertEquals(attackedPositions.size(), 2);
        Assert.assertTrue(attackedPositions.contains(builder.at("D", "6")));
        Assert.assertTrue(attackedPositions.contains(builder.at("F", "6")));
    }
}
