import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class FactorialsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFactorials_negative_throwException() {
        Factorials.factorials(-1);
    }

    @Test
    public void testFactorials_zero_returnOne() {
        assertEquals(BigInteger.ONE, Factorials.factorials(0));
    }

    @Test
    public void testFactorials_positive_returnFactorial() {
        BigInteger prev = BigInteger.ONE;
        for (int i = 1; i < 100; ++i) {
            BigInteger current = Factorials.factorials(i);
            assertEquals(current, prev.multiply(BigInteger.valueOf(i)));
            prev = current;
        }
    }
}