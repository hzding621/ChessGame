package chessgame.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * Contains tests for SquareCell and SquareCell.Factory
 */
public class SquareCellTest {

    private Coordinate.Factory rowFactory;
    private Coordinate.Factory columnFactory;
    private SquareCell.Factory factory;

    @Before
    public void initializeFactory() {
        rowFactory = new Coordinate.Factory(6);
        columnFactory = new Coordinate.Factory(8);
        factory = new SquareCell.Factory(rowFactory, columnFactory);
    }

    @Test
    public void testConstructionOfSquareCellByFactory() {
        Optional<SquareCell> cell;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                cell = factory.of(i, j);
                Assert.assertTrue(cell.isPresent());
            }
        }

        cell = factory.of(6,5);
        Assert.assertFalse(cell.isPresent());

        cell = factory.of(5,8);
        Assert.assertFalse(cell.isPresent());

        cell = factory.of(-1,8);
        Assert.assertFalse(cell.isPresent());
    }

    @Test
    public void testFindDirectionWithNormalPoints() {

        SquareCell a = factory.of(2, 2).get();
        SquareCell b = factory.of(3, 3).get();
        Assert.assertEquals(SquareCell.findDirection(a, b), SquareDirection.NORTHEAST);
        Assert.assertEquals(SquareCell.findDirection(b, a), SquareDirection.SOUTHWEST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithPointsNotOnALine() {

        SquareCell a = factory.of(0,0).get();
        SquareCell b = factory.of(1,2).get();
        SquareCell.findDirection(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithSamePoints() {

        SquareCell a = factory.of(0,0).get();
        SquareCell b = factory.of(0,0).get();
        SquareCell.findDirection(a, b);
    }

    @Test
    public void testMoveOnceNormalPoint() {

        int[][] answers = new int[][] {
                {3,4}, {4,4}, {4,3}, {4, 2}, {3, 2}, {2, 2}, {2, 3}, {2, 4}
        };
        SquareDirection[] dirs = new SquareDirection[] {
                SquareDirection.NORTH, SquareDirection.NORTHEAST, SquareDirection.EAST, SquareDirection.SOUTHEAST,
                SquareDirection.SOUTH, SquareDirection.SOUTHWEST, SquareDirection.WEST, SquareDirection.NORTHWEST
        };

        SquareCell cell = factory.of(3,3).get();
        for (int i = 0; i < answers.length; i++) {
            SquareCell moved = factory.moveOnce(cell, dirs[i]).get();
            Assert.assertEquals(moved, factory.of(answers[i][0], answers[i][1]).get());
        }
    }

    @Test
    public void testMoveOnceAtEdge() {
        SquareCell cell = factory.of(0,0).get();
        Optional<SquareCell> nextCell;

        nextCell = factory.moveOnce(cell, SquareDirection.WEST);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = factory.moveOnce(cell, SquareDirection.SOUTHWEST);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = factory.moveOnce(cell, SquareDirection.SOUTH);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = factory.moveOnce(cell, SquareDirection.SOUTHEAST);
        Assert.assertFalse(nextCell.isPresent());
    }
}
