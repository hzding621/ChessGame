package chessgame.game;

import chessgame.board.Square;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import com.google.common.collect.SetMultimap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains tests for MoveFinder
 * Mainly tests for Available Moves computation
 */
public final class MoveFinderTest extends AbstractTest {

    private static boolean checkIsValidMove(SetMultimap<Square, Move<Square>> allMoves, Square source, Square target) {
        return allMoves.get(source)
                .stream()
                .filter(move -> move instanceof SimpleMove)
                .anyMatch(move -> (((SimpleMove<Square>)(move)).getTarget().equals(target)));
    }

    @Test
    public void testAvailableMovesOpeningPosition() {
        testBoth(b -> {
            hydrate(new StandardSetting(), b);
            SetMultimap<Square, Move<Square>> allMoves = moveFinder.getAvailableMoves();

            // All pawns can move one or two cells upwards
            for (int i = 0; i < 8; i++) {
                String file = String.valueOf((char)('A' + i));
                Assert.assertTrue(checkIsValidMove(allMoves, cell.at(file, "2"), cell.at(file, "3")));
                Assert.assertTrue(checkIsValidMove(allMoves, cell.at(file, "2"), cell.at(file, "4")));
            }
            // Both knights have two moves
            Assert.assertTrue(checkIsValidMove(allMoves, cell.at("B", "1"), cell.at("A", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, cell.at("B", "1"), cell.at("C", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, cell.at("G", "1"), cell.at("F", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, cell.at("G", "1"), cell.at("H", "3")));
        });
    }

    @Test
    public void testCheckmateSituation() {
        testBoth(b -> {
            // Two Rooks on 7th and 8th rank, opponent King in corner
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .piece(StandardPieces.KING, Player.BLACK, "H", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "C", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "D", "7")
                    .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square>> allMoves = moveFinder.getAvailableMoves();

            Assert.assertTrue(allMoves.isEmpty());
            Assert.assertEquals(1, runtimeInformation.getAttackInformation().getCheckers().size(), 1);
        });
    }

    @Test
    public void testStalemateSituation() {
        testBoth(b -> {
            // White king is stalemated
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .piece(StandardPieces.QUEEN, Player.BLACK, "B", "3")
                    .piece(StandardPieces.KING, Player.BLACK, "D", "3")
                    .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.WHITE)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square>> allMoves = moveFinder.getAvailableMoves();

            Assert.assertTrue(allMoves.isEmpty());
            Assert.assertTrue(runtimeInformation.getAttackInformation().getCheckers().isEmpty());
        });
    }

    @Test
    public void testCheckMateFromDoubleRookCheck() {
        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .piece(StandardPieces.KING, Player.BLACK, "H", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "F", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "H", "6")
                    .piece(StandardPieces.QUEEN, Player.BLACK, "G", "7")
                    .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square>> allMoves = moveFinder.getAvailableMoves();
            Assert.assertTrue(allMoves.isEmpty());
        });
    }

    @Test
    public void testCheckMateCheckAndDiscoveredCheck() {
        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .piece(StandardPieces.KING, Player.BLACK, "H", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "F", "8")
                    .piece(StandardPieces.ROOK, Player.WHITE, "H", "4")
                    .piece(StandardPieces.QUEEN, Player.BLACK, "H", "6")
                    .piece(StandardPieces.KING, Player.WHITE, "A", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);
        });
    }
}
