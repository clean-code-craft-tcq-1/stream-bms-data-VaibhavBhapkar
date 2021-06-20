package receiver;

import org.junit.Test;

import junit.framework.Assert;

public class MovingAverageTest {
    MovingAverage movingAverage = new MovingAverage(3);

    @Test
    public void testMovingAverage() {
        movingAverage.next(45);
        movingAverage.next(20);
        movingAverage.next(3);
        Assert.assertEquals(9.0, movingAverage.next(4));
    }
}
