package chessgame.board;

import chessgame.game.GameSetting;
import chessgame.game.StandardSetting;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utility.CollectionUtils;
import utility.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contains tests for ChessBoard
 */
public final class RectangularBoardTest {

    private static class Instance<P extends PieceClass> extends RectangularBoard<P> {

        private Instance(Map<Square, Piece<P>> occupants, Square.Builder cellBuilder) {
            super(occupants, cellBuilder);
        }

        public static <P extends PieceClass> Instance<P> create(
                GameSetting.GridGame<Square, P> gameSetting) {
            final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
            final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
            return new Instance<>(gameSetting.constructPiecesByStartingPosition(),
                    new Square.Builder(fileBuilder, rankBuilder));
        }
    }

    private Square.Builder builder;
    private Instance<StandardPieces> testBoard;

    @Before
    public void instantiateTestPieceSet() {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        testBoard = Instance.create(StandardSetting.VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovePieceOutOfBoundary() {

        Coordinate.Builder builder = new Coordinate.Builder(10);
        Square.Builder maliciousBuilder = new Square.Builder(builder, builder);
        testBoard.movePiece(maliciousBuilder.at("E", "2"), maliciousBuilder.at("E", "9"));
    }

    @Test
    public void testFurthestReach() {
        // (B2) [B3 -> B7]
        List<Square> path = testBoard.travelUntilBlocked(builder.at("B", "2"), TwoDimension.NORTH, StepSize.of(1, 0), false, true);
        Assert.assertEquals(5, path.size());
        Assert.assertEquals(builder.at("B", "3"), path.get(0));
        Assert.assertEquals(builder.at("B", "7"), CollectionUtils.last(path).get());

        // (B2) [C3 -> G7]
        path = testBoard.travelUntilBlocked(builder.at("B", "2"), TwoDimension.NORTHEAST, StepSize.of(1, 0), false, true);
        Assert.assertEquals(5, path.size());
        Assert.assertEquals(builder.at("C", "3"), path.get(0));
        Assert.assertEquals(builder.at("G", "7"), CollectionUtils.last(path).get());

        // (B2) [B1]
        path = testBoard.travelUntilBlocked(builder.at("B", "2"), TwoDimension.SOUTH, StepSize.of(1, 0), false, true);
        Assert.assertEquals(1, path.size());
        Assert.assertEquals(builder.at("B", "1"), path.get(0));
    }

    @Test
    public void testFirstAndSecondOccupant() {

        // Move white Queen to E4
        // - pinning black Pawn at E7, black King at E8,
        // - pinning black Pawn at B7, black Rook at A8
        testBoard.movePiece(builder.at("D", "1"), builder.at("E", "4"));

        Optional<Pair<Square, Square>> pin = testBoard.firstTwoEncounters(builder.at("E", "4"), TwoDimension.NORTH, StepSize.of(1,0));
        Assert.assertTrue(pin.isPresent());
        Assert.assertEquals(builder.at("E", "7"), pin.get().first());
        Assert.assertEquals(builder.at("E", "8"), pin.get().second());

        pin = testBoard.firstTwoEncounters(builder.at("E", "4"), TwoDimension.NORTHWEST, StepSize.of(1,0));
        Assert.assertTrue(pin.isPresent());
        Assert.assertEquals(builder.at("B", "7"), pin.get().first());
        Assert.assertEquals(builder.at("A", "8"), pin.get().second());

    }

    @Test
    public void testAttackPawnStyle() {
        Collection<Square> attackedPositions = testBoard.attackPawnStyle(builder.at("E", "2"), Player.WHITE);
        Assert.assertEquals(2, attackedPositions.size());
        Assert.assertTrue(attackedPositions.contains(builder.at("D", "3")));
        Assert.assertTrue(attackedPositions.contains(builder.at("F", "3")));

        attackedPositions = testBoard.attackPawnStyle(builder.at("E", "7"), Player.BLACK);
        Assert.assertEquals(2, attackedPositions.size());
        Assert.assertTrue(attackedPositions.contains(builder.at("D", "6")));
        Assert.assertTrue(attackedPositions.contains(builder.at("F", "6")));
    }
}
