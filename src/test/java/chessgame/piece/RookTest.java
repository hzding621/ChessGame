package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
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
public final class RookTest {

    private Square.Builder builder;
    private ChessBoard testBoard;
    private PieceInformation<Square, StandardPieces> pieceInformation;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
    }

    @Test
    public void testAttacking() {
        // white rook at E3 attacking A3, B3, C3, D3, F3, G3, H3, E1, E2, E4, E5
        // white king at A1
        // black king at A8
        // black pawn at E5

        testBoard = new ChessBoard(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.ROOK, Player.WHITE, "E", "2")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .piece(StandardPieces.KING, Player.BLACK, "E", "8")
                .piece(StandardPieces.PAWN, Player.BLACK, "E", "5")
                .build()
        );

        Collection<Square> attacked =
                new Rook.RookRule<>(testBoard).attacking(builder.at("E", "2"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 11);

        // after removing Pawn E5, also attacking E6, E7, E8
        testBoard.clearPiece(builder.at("E", "5"));

        attacked = new Rook.RookRule<>(testBoard).attacking(builder.at("E", "2"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 14);
        Assert.assertTrue(attacked.contains(builder.at("E", "6")));
        Assert.assertTrue(attacked.contains(builder.at("E", "7")));
        Assert.assertTrue(attacked.contains(builder.at("E", "8")));
    }

    @Test
    public void testLatentAttacking() {
        // white rook at E3
        // white pawn at E5
        // black king at E7
        // black pawn at C3
        // black pawn at A3
        // white king at E1

        // latent attack, E7 via E5, A3 via C3

        testBoard = new ChessBoard(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.ROOK, Player.WHITE, "E", "3")
                .piece(StandardPieces.PAWN, Player.WHITE, "E", "5")
                .piece(StandardPieces.KING, Player.BLACK, "E", "7")
                .piece(StandardPieces.PAWN, Player.BLACK, "C", "3")
                .piece(StandardPieces.PAWN, Player.BLACK, "A", "3")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .build()
        );

        Collection<LatentAttack<Square>> attacked =
                new Rook.RookRule<>(testBoard).latentAttacking(builder.at("E", "3"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 2);
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("E", "7"))
                && at.getBlocker().equals(builder.at("E", "5"))));
        Assert.assertTrue(attacked.stream().anyMatch(at -> at.getAttacked().equals(builder.at("A", "3"))
                && at.getBlocker().equals(builder.at("C", "3"))));
    }
}