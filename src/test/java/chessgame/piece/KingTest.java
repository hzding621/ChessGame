package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.Square;
import chessgame.game.DefenderInformation;
import chessgame.game.PieceInformation;
import chessgame.game.StandardSetting;
import chessgame.move.Castling;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

/**
 * Contains test for King.
 * This class does NOT test check logic, which is handled by rules class
 */
@RunWith(MockitoJUnitRunner.class)
public class KingTest {

    private Square.Builder builder;
    private ChessBoard testBoard = new ChessBoard(new StandardSetting());
    @Mock private PieceInformation<Square, StandardPieces> pieceInformation;
    @Mock private DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        Mockito.when(pieceInformation.locateKing(Player.WHITE)).thenReturn(builder.at("E", "1"));
        Mockito.when(pieceInformation.locateKing(Player.BLACK)).thenReturn(builder.at("E", "8"));
    }

    @Test
    public void testAttacking() {

        Collection<Square> attacked =
                new King.KingRule<>(testBoard, pieceInformation).attacking(builder.at("E", "1"), Player.WHITE);
        Assert.assertEquals(5, attacked.size());

        testBoard.movePiece(builder.at("E", "1"), builder.at("E", "4"));
        attacked = new King.KingRule<>(testBoard, pieceInformation).attacking(builder.at("E", "4"), Player.WHITE);
        Assert.assertEquals(8, attacked.size());
    }

    @Test
    public void testCastling() {
        Mockito.when(defenderInformation.isAttacked(Mockito.any(Square.class))).thenReturn(false);

        // Empty the spaces between king and queen-side rook
        testBoard.movePiece(builder.at("D", "1"), builder.at("D", "3"));
        testBoard.movePiece(builder.at("C", "1"), builder.at("C", "3"));
        testBoard.movePiece(builder.at("B", "1"), builder.at("B", "3"));

        Collection<Move<Square>> move = new King.KingRuleWithCastling(testBoard, pieceInformation,
                defenderInformation).specialMove(Player.WHITE);
        Assert.assertTrue(move.contains(new Castling<>(
                SimpleMove.of(builder.at("E", "1"), builder.at("C", "1"), Player.WHITE),
                SimpleMove.of(builder.at("A", "1"), builder.at("D", "1"), Player.WHITE)
                )));
    }
}