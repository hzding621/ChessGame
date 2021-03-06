package core.game;

import core.board.GridTileBuilder;
import core.board.Square;
import core.board.TwoDimension;
import core.move.CastlingMove;
import core.move.Move;
import core.move.SimpleMove;
import core.piece.StandardPieces;
import core.rule.StandardRuleBindings;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Test the computation of available moves in a standard chess game
 */
public class ChessGameTest {

    private ChessGame<StandardPieces> game;
    private GridTileBuilder<Square, TwoDimension> tile;

    @Before
    public void initializeGame() {
        game = ChessGame.create(StandardSetting.VALUE, (StandardRuleBindings::new));
        tile = game.getBoard().getGridTileBuilder();
    }

    @Test
    public void testEscapingCheck() {

        int moves = 0;

        for (String[] move: new String[][] {
                {"D", "2", "D", "4"},
                {"D", "7", "D", "5"},
                {"E", "2", "E", "4"},
                {"G", "8", "F", "6"},
                {"F", "1", "B", "5"},

        }) {
            moves++;
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(6, game.availableMoves().size());
        Assert.assertEquals(2, game.availableMovesFrom(tile.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("C", "7")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("C", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("D", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("F", "6")).size());

        for (String[] move:  new String[][] {
                {"C", "7", "C", "6"},
                {"B", "5", "C", "6"}
        }) {
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(6, game.availableMoves().size());
        Assert.assertEquals(2, game.availableMovesFrom(tile.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("B", "7")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("C", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("D", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("F", "6")).size());

        for (String[] move:  new String[][] {
                {"D", "8", "D", "7"},
                {"C", "6", "D", "7"}
        }) {
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertEquals(5, game.availableMoves().size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("B", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("C", "8")).size());
        Assert.assertEquals(2, game.availableMovesFrom(tile.at("E", "8")).size());
        Assert.assertEquals(1, game.availableMovesFrom(tile.at("F", "6")).size());
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
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Assert.assertTrue(game.availableMoves().isEmpty());
    }

    @Test
    public void testCastling() {
        for (String[] move: new String[][] {
                {"E", "2", "E", "4"},
                {"E", "7", "E", "5"},
                {"G", "1", "F", "3"},
                {"G", "8", "F", "6"},
                {"F", "1", "C", "4"},
                {"B", "8", "C", "6"},
        }) {
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Collection<Move<Square, StandardPieces>> moves = game.availableMovesFrom(tile.at("E", "1"));
        Assert.assertEquals(3, moves.size());

        Optional<Move<Square, StandardPieces>> castling = Iterables.tryFind(moves, m -> m instanceof CastlingMove);
        Assert.assertTrue(castling.isPresent());

        game.move(castling.get());

        Assert.assertTrue(game.getBoard().isOccupied(tile.at("G", "1")));
        Assert.assertTrue(game.getBoard().isOccupied(tile.at("F", "1")));
    }

    @Test
    public void testUndoLastRound() {
        for (String[] move: new String[][] {
                {"D", "2", "D", "4"},
                {"D", "7", "D", "5"},
                {"E", "2", "E", "4"},
                {"G", "8", "F", "6"},

        }) {
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        game.undoLastRound();

        Assert.assertTrue(game.getBoard().isOccupied(tile.at("E", "2")));
        Assert.assertTrue(game.getBoard().isOccupied(tile.at("G", "8")));
        Assert.assertEquals(0, game.getRuntimeInformation().getPieceInformation().getPieceMoveCount(game.getBoard().getPiece(tile.at("E", "2")).get()));
        Assert.assertEquals(0, game.getRuntimeInformation().getPieceInformation().getPieceMoveCount(game.getBoard().getPiece(tile.at("G", "8")).get()));
    }

    @Test
    public void testUndoCastling() {
        for (String[] move: new String[][] {
                {"E", "2", "E", "4"},
                {"E", "7", "E", "5"},
                {"G", "1", "F", "3"},
                {"G", "8", "F", "6"},
                {"F", "1", "C", "4"},
                {"B", "8", "C", "6"},
        }) {
            game.move(SimpleMove.of(tile.at(move[0], move[1]), tile.at(move[2], move[3]), game.getActor()));
        }

        Collection<Move<Square, StandardPieces>> moves = game.availableMovesFrom(tile.at("E", "1"));
        Optional<Move<Square, StandardPieces>> castling = Iterables.tryFind(moves, m -> m instanceof CastlingMove);
        game.move(castling.get());

        Assert.assertTrue(game.getBoard().isOccupied(tile.at("G", "1")));
        Assert.assertTrue(game.getBoard().isOccupied(tile.at("F", "1")));

        game.undoLastRound();

        Assert.assertFalse(game.getBoard().isOccupied(tile.at("G", "1")));
        Assert.assertFalse(game.getBoard().isOccupied(tile.at("F", "1")));
        Assert.assertTrue(game.getBoard().isOccupied(tile.at("E", "1")));
        Assert.assertTrue(game.getBoard().isOccupied(tile.at("H", "1")));

    }
}
