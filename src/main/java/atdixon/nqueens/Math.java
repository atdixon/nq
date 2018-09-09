package atdixon.nqueens;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.IntStream.rangeClosed;

public final class Math {

    private Math() {}

    /** Produces combinations in monotonically increasing order. */
    static Stream<int[]> combinations(int n, int k) { // O(k)
        checkState(k > 0);
        checkState(k <= n);
        return Stream.generate(new Supplier<int[]>() {
            int x = 0, y = 1;
            @Override public int[] get() {
                final int[] next = new int[]{ x, y };
                if (y < n - 1) { ++y; }
                else { ++x; y = x + 1; }
                return next;
            }})
            .limit(countCombinations(n, k));
    }

    static long countCombinations(int n, int k) { // O(k)
        checkState(k > 0);
        checkState(k <= n);
        return rangeClosed(n - k + 1, n)
            .reduce((a, b) -> a * b).orElse(1) /
            rangeClosed(1, k)
                .reduce((a, b) -> a * b)
                .orElse(1);
    }

    /** Precondition: provided xs must be monotonically increasing. */
    static boolean isColinear(int... xys) { // O(n)
        if (xys.length < 3)
            return true;
        final int
            x1 = xys[0], y1 = xys[1],
            x2 = xys[2], y2 = xys[3];
        final int mn = y2 - y1, md = x2 - x1;
        for (int i = 4; i < xys.length; i += 2) {
            final int
                xi = xys[i],
                yi = xys[i + 1];
            if ((mn * (xi - x2) + md * y2) % md != 0
                || (mn * (xi - x2) + md * y2) / md != yi) {
                return false;
            }
        }
        return true;
    }

}
