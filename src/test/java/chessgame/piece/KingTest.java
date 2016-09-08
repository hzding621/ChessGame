package chessgame.piece;

import chessgame.board.ChessBoard;
import chessgame.board.Coordinate;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.game.ConfigurableGameSetting;
import chessgame.game.DefenderInformation;
import chessgame.game.PieceInformation;
import chessgame.game.StandardSetting;
import chessgame.move.Castling;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

/**
 * Contains test for King.
 * This class does NOT test check logic, which is handled by rules class
 */
public class KingTest {

    private Square.Builder builder;
    private ChessBoard testBoard = new ChessBoard(new StandardSetting());
    private PieceInformation<Square, StandardPieces> pieceInformation = new PieceInformation<Square, StandardPieces>() {
        @Override
        public int getPieceMoveCount(Piece<StandardPieces> piece) {
            return 0;
        }

        @Override
        public Square locateKing(Player player) {
            return player == Player.WHITE ? builder.at("E", "1") : builder.at("E", "8");
        }
    };

    private DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
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
        defenderInformation = new DefenderInformation<Square, StandardPieces, ChessBoard>() {
            @Override
            public boolean isAttacked(Square cell) {
                return false;
            }
            @Override
            public Set<Attack<Square>> getCheckers() {
                return ImmutableSet.of();
            }
            @Override
            public Set<LatentAttack<Square>> getLatentCheckersByBlocker(Square blocker) {
                return ImmutableSet.of();
            }
        };

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