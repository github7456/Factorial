import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MultiprocessFactorialsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFactorials_negative_throwException() throws ExecutionException, InterruptedException {
        MultiprocessFactorials.factorials(-1, 100);
    }

    @Test
    public void testFactorials_zero_returnOne() throws ExecutionException, InterruptedException {
        assertEquals(BigInteger.ONE, MultiprocessFactorials.factorials(0, 100));
    }

    @Test
    public void testFactorials_negativeThread_returnFactorial() throws ExecutionException, InterruptedException {
        assertEquals(BigInteger.valueOf(120), MultiprocessFactorials.factorials(5, -2));
    }

    @Test
    public void testFactorials_positive_returnFactorial() throws ExecutionException, InterruptedException {
        for (int threadCount = 1; threadCount < 10; ++threadCount) {
            BigInteger prev = BigInteger.ONE;
            for (int i = 1; i < 100; ++i) {
                BigInteger current = MultiprocessFactorials.factorials(i, threadCount);
                assertEquals(current, prev.multiply(BigInteger.valueOf(i)));
                prev = current;
            }
        }
    }
}