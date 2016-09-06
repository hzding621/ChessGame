package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.GridCellFactory;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.piece.ChessPieceType;
import chessgame.rule.ChessRuleBindings;
import chessgame.rule.Rules;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Contains tests for ActorInformation
 * Mainly tests for Available Moves computation
 */
public final class ActorInformationTest {

    private ChessBoard testBoard;
    private GridCellFactory<Square, TwoDimension> cell;
    private Rules<Square, ChessPieceType, ChessBoard> rules;
    private BoardInformation<Square, ChessPieceType, ChessBoard> boardInformation;

    private void hydrate(GameSetting<Square, ChessPieceType> customizedSet) {
        boardInformation = new BoardInformation<>(customizedSet);
        testBoard = new ChessBoard(customizedSet);
        cell = testBoard.getGridCellFactory();
        rules = new Rules<>(new ChessRuleBindings(testBoard, boardInformation.getPieceInformation()));
        boardInformation.getDefenderInformation().refresh(testBoard, rules, boardInformation.getPlayerInformation(),
                boardInformation.getPieceInformation().locateKing(boardInformation.getActor()));
        boardInformation.getActorInformation().refresh(testBoard, rules, boardInformation.getDefenderInformation(),
                boardInformation.getPlayerInformation(), boardInformation.locateKing(boardInformation.getActor()));
    }

    @Test
    public void testAvailableMovesOpeningPosition() {
        hydrate(new StandardSetting());
        Map<Square, Set<Square>> allMoves = boardInformation.getActorInformation().getAvailableMoves();

        // All pawns can move one or two cells upwards
        for (int i = 0; i < 8; i++) {
            String file = String.valueOf((char)('A' + i));
            Assert.assertTrue(allMoves.get(cell.at(file, "2")).contains(cell.at(file, "3")));
            Assert.assertTrue(allMoves.get(cell.at(file, "2")).contains(cell.at(file, "4")));
        }

        // Both knights have two moves
        Assert.assertTrue(allMoves.get(cell.at("B", "1")).contains(cell.at("A", "3")));
        Assert.assertTrue(allMoves.get(cell.at("B", "1")).contains(cell.at("C", "3")));
        Assert.assertTrue(allMoves.get(cell.at("G", "1")).contains(cell.at("F", "3")));
        Assert.assertTrue(allMoves.get(cell.at("G", "1")).contains(cell.at("H", "3")));
    }
}
