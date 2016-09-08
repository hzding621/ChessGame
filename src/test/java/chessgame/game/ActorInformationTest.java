package chessgame.game;

import chessgame.board.*;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import chessgame.rule.BasicRuleBindings;
import chessgame.rule.Rules;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains tests for ActorInformation
 * Mainly tests for Available Moves computation
 */
public final class ActorInformationTest {

    private RectangularBoard<StandardPieces> testBoard;
    private GridCellFactory<Square, TwoDimension> cell;
    private Rules<Square, StandardPieces, RectangularBoard<StandardPieces>> rules;
    private BoardInformation<Square, StandardPieces, RectangularBoard<StandardPieces>> boardInformation;

    private void hydrate(GameSetting.GridGame<Square, StandardPieces> customizedSet) {
        boardInformation = new BoardInformation<>(customizedSet);
        testBoard = new RectangularBoard<>(customizedSet);
        cell = testBoard.getGridCellFactory();
        rules = new Rules<>(new BasicRuleBindings<>(testBoard, boardInformation));
        boardInformation.updateInformationForThisRound(testBoard, rules, ImmutableList::of, true);
    }

    private static boolean checkIsValidMove(SetMultimap<Square, Move<Square>> allMoves, Square source, Square target) {
        return allMoves.get(source)
                .stream()
                .filter(move -> move instanceof SimpleMove)
                .anyMatch(move -> (((SimpleMove<Square>)(move)).getTarget().equals(target)));
    }

    @Test
    public void testAvailableMovesOpeningPosition() {
        hydrate(new StandardSetting());
        SetMultimap<Square, Move<Square>> allMoves = boardInformation.getAvailableMoves();

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
    }

    @Test
    public void testCheckmateSituation() {
        // Two Rooks on 7th and 8th rank, opponent King in corner
        GameSetting.GridGame<Square, StandardPieces> endGame = ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.KING, Player.BLACK, "H", "8")
                .piece(StandardPieces.ROOK, Player.WHITE, "C", "8")
                .piece(StandardPieces.ROOK, Player.WHITE, "D", "7")
                .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                .starter(Player.BLACK)
                .build();
        hydrate(endGame);

        SetMultimap<Square, Move<Square>> allMoves = boardInformation.getAvailableMoves();

        Assert.assertTrue(allMoves.isEmpty());
        Assert.assertEquals(1,boardInformation.getCheckers().size(), 1);
    }

    @Test
    public void testStalemateSituation() {
        // White king is stalemated
        GameSetting.GridGame<Square, StandardPieces> endGame = ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.QUEEN, Player.BLACK, "B", "3")
                .piece(StandardPieces.KING, Player.BLACK, "D", "3")
                .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                .starter(Player.WHITE)
                .build();
        hydrate(endGame);

        SetMultimap<Square, Move<Square>> allMoves = boardInformation.getAvailableMoves();

        Assert.assertTrue(allMoves.isEmpty());
        Assert.assertTrue(boardInformation.getCheckers().isEmpty());
    }
}
