package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.GridCellFactory;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
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
    private Rules<Square, StandardPieces, ChessBoard> rules;
    private BoardInformation<Square, StandardPieces, ChessBoard> boardInformation;

    private void hydrate(GameSetting<Square, StandardPieces> customizedSet) {
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

    @Test
    public void testCheckmateSituation() {
        // Two Rooks on 7th and 8th rank, opponent King in corner
        GameSetting<Square, StandardPieces> endGame = ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.KING, Player.BLACK, "H", "8")
                .piece(StandardPieces.ROOK, Player.WHITE, "C", "8")
                .piece(StandardPieces.ROOK, Player.WHITE, "D", "7")
                .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                .starter(Player.BLACK)
                .build();
        hydrate(endGame);

        Map<Square, Set<Square>> allMoves = boardInformation.getActorInformation().getAvailableMoves();

        Assert.assertTrue(allMoves.isEmpty());
        Assert.assertEquals(boardInformation.getDefenderInformation().getCheckers().size(), 1);
    }

    @Test
    public void testStalemateSituation() {
        // White king is stalemated
        GameSetting<Square, StandardPieces> endGame = ConfigurableGameSetting.builder(8, 8)
                .piece(StandardPieces.QUEEN, Player.BLACK, "B", "3")
                .piece(StandardPieces.KING, Player.BLACK, "D", "3")
                .piece(StandardPieces.KING, Player.WHITE, "C", "1")
                .starter(Player.WHITE)
                .build();
        hydrate(endGame);

        Map<Square, Set<Square>> allMoves = boardInformation.getActorInformation().getAvailableMoves();

        Assert.assertTrue(allMoves.isEmpty());
        Assert.assertTrue(boardInformation.getDefenderInformation().getCheckers().isEmpty());
    }
}
