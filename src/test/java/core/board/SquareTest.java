package core.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * Contains tests for Square and Square.Builder
 */
public class SquareTest {

    private Coordinate.Builder rowBuilder;
    private Coordinate.Builder columnBuilder;
    private Square.Builder builder;

    @Before
    public void initializeFactory() {
        rowBuilder = new Coordinate.Builder(6);
        columnBuilder = new Coordinate.Builder(8);
        builder = new Square.Builder(rowBuilder, columnBuilder);
    }

    @Test
    public void testFindDirectionWithNormalPoints() {

        Square a = builder.at(2, 2);
        Square b = builder.at(3, 3);
        Assert.assertEquals(TwoDimension.NORTHEAST, Square.findDirection(a, b));
        Assert.assertEquals(TwoDimension.SOUTHWEST, Square.findDirection(b, a));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithPointsNotOnALine() {

        Square a = builder.at(0,0);
        Square b = builder.at(1,2);
        Square.findDirection(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDirectionWithSamePoints() {

        Square a = builder.at(0,0);
        Square b = builder.at(0,0);
        Square.findDirection(a, b);
    }

    @Test
    public void testMoveOnceNormalPoint() {

        int[][] answers = new int[][] {
                {3,4}, {4,4}, {4,3}, {4, 2}, {3, 2}, {2, 2}, {2, 3}, {2, 4}
        };
        TwoDimension[] dirs = new TwoDimension[] {
                TwoDimension.NORTH, TwoDimension.NORTHEAST, TwoDimension.EAST, TwoDimension.SOUTHEAST,
                TwoDimension.SOUTH, TwoDimension.SOUTHWEST, TwoDimension.WEST, TwoDimension.NORTHWEST
        };

        Square tile = builder.at(3,3);
        for (int i = 0; i < answers.length; i++) {
            Square moved = builder.moveOnce(tile, dirs[i], StepSize.of(1,0)).get();
            Assert.assertEquals(builder.at(answers[i][0], answers[i][1]), moved);
        }
    }

    @Test
    public void testMoveOnceAtEdge() {
        Square tile = builder.at(0,0);
        Optional<Square> nextTile;

        nextTile = builder.moveOnce(tile, TwoDimension.WEST, StepSize.of(1,0));
        Assert.assertFalse(nextTile.isPresent());

        nextTile = builder.moveOnce(tile, TwoDimension.SOUTHWEST, StepSize.of(1,0));
        Assert.assertFalse(nextTile.isPresent());

        nextTile = builder.moveOnce(tile, TwoDimension.SOUTH, StepSize.of(1,0));
        Assert.assertFalse(nextTile.isPresent());

        nextTile = builder.moveOnce(tile, TwoDimension.SOUTHEAST, StepSize.of(1,0));
        Assert.assertFalse(nextTile.isPresent());
    }
}
