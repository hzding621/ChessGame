package chessgame.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests for Coordinate and Coordinate.Builder
 */
public class CoordinateTest {

    private Coordinate.Builder builder;

    @Before
    public void initializeBuilder() {
        builder = new Coordinate.Builder(10);
    }

    @Test
    public void testConstructCoordinate() {
        Assert.assertEquals(builder.at(5).getIndex(), 5);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructOutOfRangeCoordinate() {
        builder.at(10);
        builder.at(12);
    }
}
