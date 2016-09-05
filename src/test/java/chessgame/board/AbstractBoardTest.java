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
    private SquareCell.Factory factory;

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
        Coordinate.Factory coordinateFactory = new Coordinate.Factory(4);
        factory = new SquareCell.Factory(coordinateFactory, coordinateFactory);
        testPieceSet = new PieceSet<SquareCell, KingPawnGame, Piece<KingPawnGame>>() {

            private final Piece whitePawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.WHITE, 0);
            private final Piece blackPawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.BLACK, 0);
            private final Piece whiteKing = new King<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.WHITE, 0);
            private final Piece blackKing = new King<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.BLACK, 0);

            @Override
            public Collection<KingPawnGame> getSupportedTypes() {
                return Collections.singleton(KingPawnGame.PAWN);
            }

            @Override
            public Collection<PieceLocator<SquareCell, KingPawnGame, Piece<KingPawnGame>>>
            constructPiecesOfTypeAndPlayer(KingPawnGame type, Player player) {
                return player == Player.WHITE
                        ? Arrays.asList(PieceLocator.of(factory.of(1,0).get(), whitePawn),
                                        PieceLocator.of(factory.of(2,0).get(), whiteKing))
                        : Arrays.asList(PieceLocator.of(factory.of(1,3).get(), blackPawn),
                                        PieceLocator.of(factory.of(2,3).get(), blackKing));
            }

            @Override
            public Map<Player, SquareCell> getKingStartingPositions() {
                Map<Player, SquareCell> map = new HashMap<>();
                map.put(Player.WHITE, factory.of(2, 0).get());
                map.put(Player.BLACK, factory.of(2, 3).get());
                return map;
            }

            @Override
            public Collection<Player> getPlayers() {
                return Arrays.asList(Player.WHITE, Player.BLACK);
            }
        };
    }

    @Test
    public void testConstructionOfAbstractBoard() {
        AbstractBoard<SquareCell, KingPawnGame, Piece<KingPawnGame>> testBoard = new AbstractBoard<>(this.testPieceSet);
        Piece<KingPawnGame> p = testBoard.getPiece(factory.of(1,0).get()).get();
        Assert.assertEquals(p.getPieceClass(), KingPawnGame.PAWN);
        Assert.assertEquals(p.getPlayer(), Player.WHITE);
    }
}
