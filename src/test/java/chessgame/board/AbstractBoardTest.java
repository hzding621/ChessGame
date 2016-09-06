package chessgame.board;

import chessgame.piece.*;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Contains tests for AbstractBoard
 */
public class AbstractBoardTest {

    private PieceSet<SquareCell, KingPawnGame, Piece<KingPawnGame>> testPieceSet;
    private SquareCell.Builder builder;
    AbstractBoard<SquareCell, KingPawnGame, Piece<KingPawnGame>> testBoard;
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
        builder = new SquareCell.Builder(coordinateBuilder, coordinateBuilder);
        testPieceSet = new PieceSet<SquareCell, KingPawnGame, Piece<KingPawnGame>>() {

            private final Piece whitePawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.WHITE, 0);
            private final Piece blackPawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.BLACK, 0);
            private final Piece whiteKing = new King<SquareCell, KingPawnGame>(KingPawnGame.KING, Player.WHITE, 0);
            private final Piece blackKing = new King<SquareCell, KingPawnGame>(KingPawnGame.KING, Player.BLACK, 0);

            @Override
            public Collection<KingPawnGame> getSupportedTypes() {
                return Collections.singleton(KingPawnGame.PAWN);
            }

            @Override
            public Collection<PieceLocator<SquareCell, KingPawnGame, Piece<KingPawnGame>>>
            constructPiecesOfTypeAndPlayer(KingPawnGame type, Player player) {
                return player == Player.WHITE
                        ? Arrays.asList(PieceLocator.of(builder.at(1,0), whitePawn),
                                        PieceLocator.of(builder.at(2,0), whiteKing))
                        : Arrays.asList(PieceLocator.of(builder.at(1,3), blackPawn),
                                        PieceLocator.of(builder.at(2,3), blackKing));
            }

            @Override
            public Map<Player, SquareCell> getKingStartingPositions() {
                Map<Player, SquareCell> map = new HashMap<>();
                map.put(Player.WHITE, builder.at(2, 0));
                map.put(Player.BLACK, builder.at(2, 3));
                return map;
            }

            @Override
            public Collection<Player> getPlayers() {
                return Arrays.asList(Player.WHITE, Player.BLACK);
            }
        };
        testBoard = new AbstractBoard<>(this.testPieceSet);
    }

    @Test
    public void testGetPiece() {
        Piece<KingPawnGame> p = testBoard.getPiece(builder.at(1,0)).get();
        Assert.assertEquals(p.getPieceClass(), KingPawnGame.PAWN);
        Assert.assertEquals(p.getPlayer(), Player.WHITE);

        Assert.assertFalse(testBoard.getPiece(builder.at(0,0)).isPresent());
    }

    @Test
    public void testMovePieceToEmptyCell() {
        Piece<KingPawnGame> p = testBoard.movePiece(builder.at(1, 0), builder.at(1, 1));
        Assert.assertFalse(testBoard.getPiece(builder.at(1, 0)).isPresent());
        Assert.assertEquals(testBoard.getPiece(builder.at(1,1)).get(), p);
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
        Collection<PieceLocator<SquareCell, KingPawnGame, Piece<KingPawnGame>>> locators =
                testBoard.getPiecesForPlayer(KingPawnGame.KING, Player.WHITE);
        Assert.assertEquals(locators.size(), 1);
    }

    @Test
    public void testGetAllPiecesForPlayer() {
        Collection<PieceLocator<SquareCell, KingPawnGame, Piece<KingPawnGame>>> locators =
                testBoard.getAllPiecesForPlayer(Player.WHITE);
        Assert.assertEquals(locators.size(), 2);
    }

}
