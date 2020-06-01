import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MultiprocessFactorials implements Runnable {

    private final long start;
    private final long end;
    private BigInteger result;

    private MultiprocessFactorials(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        result = intervalMultiply(start, end);
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

    public static BigInteger factorials(long n, int threadCount) throws ExecutionException, InterruptedException {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("The input %d is negative", n));
        }

        if (n <= 1) {
            return BigInteger.ONE;
        }

        threadCount = Math.max(threadCount, 1);
        if (threadCount > n) {
            threadCount = (int) n;
        }

        long[] sizes = new long[threadCount];
        long size = n / threadCount;
        long remainder = n % threadCount;
        for (int i = 0; i < threadCount; ++i) {
            sizes[i] = size;
            if (remainder > 0) {
                sizes[i]++;
                remainder--;
            }
        }

        ArrayList<Future<MultiprocessFactorials>> futures = new ArrayList<>(threadCount);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        long start = 1;
        for (int i = 0; i < threadCount; ++i) {
            long end = start + sizes[i] - 1;
            MultiprocessFactorials runnable = new MultiprocessFactorials(start, end);
            futures.add(executor.submit(runnable, runnable));
            start = end + 1;
        }

        BigInteger ans = BigInteger.ONE;
        for (Future<MultiprocessFactorials> future : futures) {
            ans = ans.multiply(future.get().result);
        }

        executor.shutdown();
        return ans;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        for (int i = 2; i < 5; ++i) {
            for (int n = 100000; n < 500000; n += 100000) {
                long startTime = System.currentTimeMillis();
                MultiprocessFactorials.factorials(n, i);
                System.out.println(String.format("N: %d, Thread count: %d, Time: %dms", n, i, System.currentTimeMillis() - startTime));
            }
        }
    }
}
