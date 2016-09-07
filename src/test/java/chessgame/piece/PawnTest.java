package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.Square;
import chessgame.game.ConfigurableGameSetting;
import chessgame.game.PieceInformation;
import chessgame.move.MoveResult;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for Pawn
 */
public final class PawnTest {

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
        // white pawn at E2
        // white king at E1
        // black king at E8

        testBoard = new ChessBoard(ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.PAWN, Player.WHITE, "E", "2")
                .piece(StandardPieces.KING, Player.WHITE, "E", "1")
                .piece(StandardPieces.KING, Player.BLACK, "E", "8")
                .build()
        );

        pieceInformation= new PieceInformation<Square, StandardPieces>() {
            @Override
            public int getPieceMoveCount(Piece<StandardPieces> piece) {
                return 0;
            }

            @Override
            public Square locateKing(Player player) {
                return player == Player.WHITE ? builder.at("E", "1") : builder.at("E", "8");
            }
        };

        Collection<Square> attacked =
                new Pawn.PawnRule<>(testBoard, pieceInformation).attacking(builder.at("D", "4"), Player.WHITE);
        Assert.assertEquals(attacked.size(), 2);
    }
}