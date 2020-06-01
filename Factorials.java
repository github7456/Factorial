import java.math.BigInteger;

public class Factorials {

    private Factorials() {}

    /**
     * Calculates the factorial of a given number n.
     *
     * The time complexity is O(n).
     *
     * @param n whose factorial to calculated.
     * @return The factorial of n.
     */
    public static BigInteger factorials(long n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("The input %d is negative", n));
        }

        return intervalMultiply(1, n);
    }

    private static BigInteger intervalMultiply(long start, long end) {
        if (start == end) {
            return BigInteger.valueOf(start);
        }

        if (start > end) {
            return BigInteger.ONE;
        }

        return intervalMultiply(start, (start + end) / 2).multiply(intervalMultiply((start + end) / 2 + 1, end));
    }

    public static void main(String[] args) {
        for (int n = 100000; n < 500000; n += 100000) {
            long startTime = System.currentTimeMillis();
            Factorials.factorials(n);
            System.out.println(String.format("N: %d, Thread count: 1, Time: %dms", n, System.currentTimeMillis() - startTime));
        }
    }
}
