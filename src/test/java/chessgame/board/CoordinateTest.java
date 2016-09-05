package chessgame.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests for Coordinate and Coordinate.Factory
 */
public class CoordinateTest {

    private Coordinate.Factory factory;

    @Before
    public void initializeFactory() {
        factory = new Coordinate.Factory(10);
    }

    @Test
    public void testConstructCoordinate() {
        Assert.assertTrue(factory.of(5).isPresent());
        Assert.assertEquals(factory.of(5).get().getIndex(), 5);
    }

    @Test
    public void testConstructOutOfRangeCoordinate() {
        Assert.assertFalse(factory.of(10).isPresent());
        Assert.assertFalse(factory.of(12).isPresent());
    }
}
