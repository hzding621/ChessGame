package chessgame.game;

import chessgame.board.GridCellFactory;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.move.SimpleMove;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Test the computation of available moves in a standard chess game
 */
public class StandardGameTest {

    private StandardGame game;
    private GridCellFactory<Square, TwoDimension> cell;

    @Before
    public void initializeGame() {
        game = StandardGame.constructGame();
        cell = game.getBoard().getGridCellFactory();
    }

    @Test
    public void testEscapingCheck() {

        for (String[] move: new String[][] {
                {"D", "2", "D", "4"},
                {"D", "7", "D", "5"},
                {"E", "2", "E", "4"},
                {"G", "8", "F", "6"},
                {"F", "1", "B", "5"},

        }) {
            game.move(SimpleMove.of(cell.at(move[0], move[1]), cell.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(6, game.availableMoves().size());
        Assert.assertEquals(2, game.availableMovesFrom(cell.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("C", "7")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("C", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("D", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("F", "6")).size());

        for (String[] move:  new String[][] {
                {"C", "7", "C", "6"},
                {"B", "5", "C", "6"}
        }) {
            game.move(SimpleMove.of(cell.at(move[0], move[1]), cell.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(6, game.availableMoves().size());
        Assert.assertEquals(2, game.availableMovesFrom(cell.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("B", "7")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("C", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("D", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("F", "6")).size());

        for (String[] move:  new String[][] {
                {"D", "8", "D", "7"},
                {"C", "6", "D", "7"}
        }) {
            game.move(SimpleMove.of(cell.at(move[0], move[1]), cell.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(5, game.availableMoves().size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("C", "8")).size());
        Assert.assertEquals(2, game.availableMovesFrom(cell.at("E", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(cell.at("F", "6")).size());
    }

    @Test
    public void testScholarsMate() {
        for (String[] move: new String[][] {
                {"E", "2", "E", "4"},
                {"E", "7", "E", "5"},
                {"D", "1", "H", "5"},
                {"B", "8", "C", "6"},
                {"F", "1", "C", "4"},
                {"G", "8", "F", "6"},
                {"H", "5", "F", "7"},
        }) {
            game.move(SimpleMove.of(cell.at(move[0], move[1]), cell.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertTrue(game.availableMoves().isEmpty());
    }
}
