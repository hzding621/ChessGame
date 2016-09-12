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

    private static boolean checkIsValidMove(SetMultimap<Square, Move<Square, StandardPieces>> allMoves, Square source, Square target) {
        return allMoves.get(source)
                .stream()
                .filter(move -> move instanceof SimpleMove)
                .anyMatch(move -> (((SimpleMove<Square, StandardPieces>)(move)).getTarget().equals(target)));
    }

    @Test
    public void testAvailableMovesOpeningPosition() {
        testBoth(b -> {
            hydrate(StandardSetting.VALUE, b);
            SetMultimap<Square, Move<Square, StandardPieces>> allMoves = moveFinder.getAvailableMoves();

            // All pawns can move one or two tiles upwards
            for (int i = 0; i < 8; i++) {
                String file = String.valueOf((char)('A' + i));
                Assert.assertTrue(checkIsValidMove(allMoves, tile.at(file, "2"), tile.at(file, "3")));
                Assert.assertTrue(checkIsValidMove(allMoves, tile.at(file, "2"), tile.at(file, "4")));
            }
            // Both knights have two moves
            Assert.assertTrue(checkIsValidMove(allMoves, tile.at("B", "1"), tile.at("A", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, tile.at("B", "1"), tile.at("C", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, tile.at("G", "1"), tile.at("F", "3")));
            Assert.assertTrue(checkIsValidMove(allMoves, tile.at("G", "1"), tile.at("H", "3")));
        });
    }

    @Test
    public void testCheckmateSituation() {
        testBoth(b -> {
            // Two Rooks on 7th and 8th rank, opponent King in corner
            ConfigurableGameSetting<StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .set(StandardPieces.KING, Player.BLACK, "H", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "C", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "D", "7")
                    .set(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square, StandardPieces>> allMoves = moveFinder.getAvailableMoves();

            Assert.assertTrue(allMoves.isEmpty());
            Assert.assertEquals(1, runtimeInformation.getAttackInformation().getCheckers().size(), 1);
        });
    }

    @Test
    public void testStalemateSituation() {
        testBoth(b -> {
            // White king is stalemated
            ConfigurableGameSetting<StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .set(StandardPieces.QUEEN, Player.BLACK, "B", "3")
                    .set(StandardPieces.KING, Player.BLACK, "D", "3")
                    .set(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.WHITE)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square, StandardPieces>> allMoves = moveFinder.getAvailableMoves();

            Assert.assertTrue(allMoves.isEmpty());
            Assert.assertTrue(runtimeInformation.getAttackInformation().getCheckers().isEmpty());
        });
    }

    @Test
    public void testCheckMateFromDoubleRookCheck() {
        testBoth(b -> {
            ConfigurableGameSetting<StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .set(StandardPieces.KING, Player.BLACK, "H", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "F", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "H", "6")
                    .set(StandardPieces.QUEEN, Player.BLACK, "G", "7")
                    .set(StandardPieces.KING, Player.WHITE, "C", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            SetMultimap<Square, Move<Square, StandardPieces>> allMoves = moveFinder.getAvailableMoves();
            Assert.assertTrue(allMoves.isEmpty());
        });
    }

    @Test
    public void testCheckMateCheckAndDiscoveredCheck() {
        testBoth(b -> {
            ConfigurableGameSetting<StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(8, 8)
                    .set(StandardPieces.KING, Player.BLACK, "H", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "F", "8")
                    .set(StandardPieces.ROOK, Player.WHITE, "H", "4")
                    .set(StandardPieces.QUEEN, Player.BLACK, "H", "6")
                    .set(StandardPieces.KING, Player.WHITE, "A", "1")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);
        });
    }
}
