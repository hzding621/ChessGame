package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.game.ConfigurableGameSetting;
import chessgame.game.PieceInformation;
import chessgame.move.MoveResult;
import chessgame.player.Player;
import chessgame.rule.LatentAttack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Pawn
 */
public final class BishopTest {

    private Square.Builder builder;
    private RectangularBoard<StandardPieces> testBoard;
    private PieceInformation<Square, StandardPieces> pieceInformation;

    @Before
    public void instantiateTestPieceSet() {
        // Create a 5x5 board for ease of testing

        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(5);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
    }

    @Test
    public void testAttacking() {
        // white bishop at center (C3) attacking A1, A5, B2, B4, D2, D4, E1, E5
        // white king at A1
        // black king at A5

        testBoard = new RectangularBoard<StandardPieces>(ConfigurableGameSetting.builder(5, 5)
                .piece(StandardPieces.BISHOP, Player.WHITE, "C", "3")
                .piece(StandardPieces.KING, Player.WHITE, "A", "1")
                .piece(StandardPieces.KING, Player.BLACK, "A", "5")
                .build()
        );

        Collection<Square> attacked =
                new Bishop.BishopRule<>(testBoard).attacking(builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());

        // after add Pawn B2, B4, no longer attack A1, A5
        testBoard.addPiece(builder.at("B", "2"), new Pawn<>(StandardPieces.PAWN, Player.WHITE, 0));
        testBoard.addPiece(builder.at("B", "4"), new Pawn<>(StandardPieces.PAWN, Player.WHITE, 1));

        attacked = new Bishop.BishopRule<>(testBoard).attacking(builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(6, attacked.size());
    }

    @Test
    public void testLatentAttacking() {
        // white bishop at center (C3)
        // white king at A1
        // black king at A5
        // white pawn at B4

        // latent attack A5 king

        testBoard = new RectangularBoard<>(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.BISHOP, Player.WHITE, "C", "3")
                .piece(StandardPieces.KING, Player.WHITE, "A", "1")
                .piece(StandardPieces.KING, Player.BLACK, "A", "5")
                .piece(StandardPieces.PAWN, Player.WHITE, "B", "4")
                .build()
        );

        Collection<LatentAttack<Square>> attacked =
                new Bishop.BishopRule<>(testBoard).latentAttacking(builder.at("C", "3"), Player.WHITE);
        Assert.assertEquals(1, attacked.size());
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("A", "5"))
                && at.getBlocker().equals(builder.at("B", "4"))));
    }
}