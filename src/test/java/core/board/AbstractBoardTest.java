package core.board;

import core.game.GameSetting;
import core.game.KingPawnGame;
import core.piece.KingPawn;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

/**
 * Contains tests for AbstractBoard
 */
public final class AbstractBoardTest {

    private static class Instance<C extends Tile, P extends PieceClass> extends AbstractBoard<C, P> {

        protected Instance(Map<C, Piece<P>> occupants) {
            super(occupants);
        }

        public static <C extends Tile, P extends PieceClass> Instance<C, P> create(GameSetting<C, P> gameSetting) {
            return new Instance<>(gameSetting.constructPiecesByStartingPosition());
        }
    }

    private Square.Builder builder;
    private Instance<Square, KingPawn> testBoard;
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
        testBoard = Instance.create(new KingPawnGame(builder));
    }

    @Test
    public void testGetPiece() {
        Piece<KingPawn> p = testBoard.getPiece(builder.at(1,0)).get();
        Assert.assertEquals(KingPawn.PAWN, p.getPieceClass());
        Assert.assertEquals(Player.WHITE, p.getPlayer());

        Assert.assertFalse(testBoard.getPiece(builder.at(0,0)).isPresent());
    }

    @Test
    public void testMovePieceToEmptyTile() {
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
    public void testClearPieceAtEmptyTileThrows() {
        testBoard.clearPiece(builder.at(0,0));
    }

    @Test
    public void testGetPiecesForPlayer() {
        Collection<Square> locators = testBoard.getPieceLocationsOfTypeAndPlayer(KingPawn.KING, Player.WHITE);
        Assert.assertEquals(1, locators.size());
    }

    @Test
    public void testGetAllPiecesForPlayer() {
        Collection<Square> locators = testBoard.getPieceLocationsOfPlayer(Player.WHITE);
        Assert.assertEquals(2, locators.size());
    }

}
