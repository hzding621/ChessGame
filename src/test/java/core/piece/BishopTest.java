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
 * Contains tests for Pawn
 */
public final class BishopTest {

    private Square.Builder builder;
    private ChessBoard<StandardPieces> testBoard;
    private Bishop<Square, StandardPieces, TwoDimension, ChessBoard<StandardPieces>> rule;

    @Before
    public void instantiateTestPieceSet() {
        // Create a 5x5 board for ease of testing

        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(5);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        rule = new Bishop<>();
    }

    @Test
    public void testAttacking() {
        // white bishop at center (C3) attacking A1, A5, B2, B4, D2, D4, E1, E5
        // white king at A1
        // black king at A5

        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(5, 5)
                .set(StandardPieces.BISHOP, Player.WHITE, "C", "3")
                .set(StandardPieces.KING, Player.WHITE, "A", "1")
                .set(StandardPieces.KING, Player.BLACK, "A", "5")
                .build()
        );

        Collection<Square> attacked =
                rule.attacking(testBoard, builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());

        // after add Pawn B2, B4, no longer attack A1, A5
        testBoard.addPiece(builder.at("B", "2"), new PieceImpl<>(StandardPieces.PAWN, Player.WHITE, 0));
        testBoard.addPiece(builder.at("B", "4"), new PieceImpl<>(StandardPieces.PAWN, Player.WHITE, 1));

        attacked = rule.attacking(testBoard, builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(6, attacked.size());
    }

    @Test
    public void testLatentAttacking() {
        // white bishop at center (C3)
        // white king at A1
        // black king at A5
        // white pawn at B4

        // latent attack A5 king

        testBoard = ChessBoard.create(ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                .set(StandardPieces.BISHOP, Player.WHITE, "C", "3")
                .set(StandardPieces.KING, Player.WHITE, "A", "1")
                .set(StandardPieces.KING, Player.BLACK, "A", "5")
                .set(StandardPieces.PAWN, Player.WHITE, "B", "4")
                .build()
        );

        Collection<LatentAttack<Square>> attacked =
                rule.latentAttacking(testBoard, builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(1, attacked.size());
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("A", "5"))
                && at.getBlocker().equals(builder.at("B", "4"))));
    }
}