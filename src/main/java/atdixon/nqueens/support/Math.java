package atdixon.nqueens.support;

import org.paukov.combinatorics3.Generator;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class Math {

    private Math() {}

    /** @return stream of k-combinations <em>in monotonically increasing order</em>. */
    public static Stream<List<Integer>> combinations(int n, int k) {
        return Generator.combination(
            IntStream.range(0, n).boxed().collect(toList()))
                .simple(k).stream();
    }

    /** Precondition: provided xs must be monotonically increasing. */
    public static boolean isColinear(int... xys) { // O(n)
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
