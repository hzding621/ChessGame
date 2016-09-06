package chessgame.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * Contains tests for SquareCell and SquareCell.Builder
 */
public class SquareCellTest {

    private Coordinate.Builder rowBuilder;
    private Coordinate.Builder columnBuilder;
    private SquareCell.Builder builder;

    @Before
    public void initializeFactory() {
        rowBuilder = new Coordinate.Builder(6);
        columnBuilder = new Coordinate.Builder(8);
        builder = new SquareCell.Builder(rowBuilder, columnBuilder);
    }

    @Test
    public void testFindDirectionWithNormalPoints() {

        SquareCell a = builder.at(2, 2);
        SquareCell b = builder.at(3, 3);
        Assert.assertEquals(SquareCell.findDirection(a, b), SquareDirection.NORTHEAST);
        Assert.assertEquals(SquareCell.findDirection(b, a), SquareDirection.SOUTHWEST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithPointsNotOnALine() {

        SquareCell a = builder.at(0,0);
        SquareCell b = builder.at(1,2);
        SquareCell.findDirection(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithSamePoints() {

        SquareCell a = builder.at(0,0);
        SquareCell b = builder.at(0,0);
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

        SquareCell cell = builder.at(3,3);
        for (int i = 0; i < answers.length; i++) {
            SquareCell moved = builder.moveOnce(cell, dirs[i]).get();
            Assert.assertEquals(moved, builder.at(answers[i][0], answers[i][1]));
        }
    }

    @Test
    public void testMoveOnceAtEdge() {
        SquareCell cell = builder.at(0,0);
        Optional<SquareCell> nextCell;

        nextCell = builder.moveOnce(cell, SquareDirection.WEST);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = builder.moveOnce(cell, SquareDirection.SOUTHWEST);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = builder.moveOnce(cell, SquareDirection.SOUTH);
        Assert.assertFalse(nextCell.isPresent());

        nextCell = builder.moveOnce(cell, SquareDirection.SOUTHEAST);
        Assert.assertFalse(nextCell.isPresent());
    }
}
