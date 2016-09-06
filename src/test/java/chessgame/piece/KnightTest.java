package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.SquareCell;
import chessgame.game.ConfigurableGameSetting;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Knight
 */
public final class KnightTest {

    private SquareCell.Builder builder;
    private ChessBoard testBoard;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new SquareCell.Builder(coordinateBuilder, coordinateBuilder);
    }

    @Test
    public void testAttacking() {
        // white knight at D4
        // white king at E1
        // black king at E8
        testBoard = new ChessBoard(new ConfigurableGameSetting.Builder(8, 8)
                .piece(ChessPieceType.KNIGHT, Player.WHITE, "D", "4")
                .piece(ChessPieceType.KING, Player.WHITE, "E", "1")
                .piece(ChessPieceType.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<SquareCell> attacked =
                new Knight.KnightRule<>(testBoard).attacking(builder.at("D", "4"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 8);
    }

    @Test
    public void testAttackingAtCorner() {
        // white knight at H1
        // white king at E1
        // black king at E8
        testBoard = new ChessBoard(new ConfigurableGameSetting.Builder(8, 8)
                .piece(ChessPieceType.KNIGHT, Player.WHITE, "H", "1")
                .piece(ChessPieceType.KING, Player.WHITE, "E", "1")
                .piece(ChessPieceType.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<SquareCell> attacked =
                new Knight.KnightRule<>(testBoard).attacking(builder.at("H", "1"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 2);
    }

    @Test
    public void testGetBlockingPositionWhenAttacking() {
        // white knight at F6
        // white king at E1
        // black king at E8
        testBoard = new ChessBoard(new ConfigurableGameSetting.Builder(8, 8)
                .piece(ChessPieceType.KNIGHT, Player.WHITE, "F", "6")
                .piece(ChessPieceType.KING, Player.WHITE, "E", "1")
                .piece(ChessPieceType.KING, Player.BLACK, "E", "8")
                .build()
        );
        Collection<SquareCell> blockingPositionsWhenAttacking =
                new Knight.KnightRule<>(testBoard).getBlockingPositionsWhenAttacking(
                        builder.at("F", "6"), builder.at("E", "8"), Player.WHITE);
        Assert.assertEquals(blockingPositionsWhenAttacking.size(), 2);
        Assert.assertTrue(blockingPositionsWhenAttacking.contains(builder.at("F", "6")));
        Assert.assertTrue(blockingPositionsWhenAttacking.contains(builder.at("E", "8")));
    }
}
