package chessgame.board;

import chessgame.game.KingPawnGame;
import chessgame.piece.*;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Contains tests for AbstractBoard
 */
public final class AbstractBoardTest {

    private Square.Builder builder;
    private AbstractBoard<Square, KingPawn> testBoard;
    /**
     * Create a test board like the following, where W is white pawn and B is black pawn, k is white king, K is black king
     *
     *      0BK0
     *      0000
     *      0000
     *      0Wk0
     */
    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(4);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        testBoard = new AbstractBoard<>(new KingPawnGame(builder));
    }

    @Test
    public void testGetPiece() {
        Piece<KingPawn> p = testBoard.getPiece(builder.at(1,0)).get();
        Assert.assertEquals(KingPawn.PAWN, p.getPieceClass());
        Assert.assertEquals(Player.WHITE, p.getPlayer());

        Assert.assertFalse(testBoard.getPiece(builder.at(0,0)).isPresent());
    }

    @Test
    public void testMovePieceToEmptyCell() {
        Piece<KingPawn> p = testBoard.movePiece(builder.at(1, 0), builder.at(1, 1));
        Assert.assertFalse(testBoard.getPiece(builder.at(1, 0)).isPresent());
        Assert.assertEquals(p, testBoard.getPiece(builder.at(1,1)).get());
    }

    @Test(expected = IllegalStateException.class)
    public void testMovePieceToOccupiedThrows() {
        testBoard.movePiece(builder.at(1, 0), builder.at(2, 0));
    }

    @Test
    public void testClearPiece() {
        testBoard.clearPiece(builder.at(1,0));
        Assert.assertFalse(testBoard.getPiece(builder.at(1,0)).isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void testClearPieceAtEmptyCellThrows() {
        testBoard.clearPiece(builder.at(0,0));
    }

    @Test
    public void testGetPiecesForPlayer() {
        Collection<Square> locators = testBoard.getPiecesOfTypeForPlayer(KingPawn.KING, Player.WHITE);
        Assert.assertEquals(1, locators.size());
    }

    @Test
    public void testGetAllPiecesForPlayer() {
        Collection<Square> locators = testBoard.getPiecesForPlayer(Player.WHITE);
        Assert.assertEquals(2, locators.size());
    }

}
