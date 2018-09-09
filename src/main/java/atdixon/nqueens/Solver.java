package atdixon.nqueens;

import com.google.common.base.Stopwatch;

import javax.annotation.Nullable;
import java.util.function.Function;

import static java.lang.Math.abs;

public class Solver {

    // discuss: why immutable/persistent! LinkedHashSet
    // guava vs vavr
    // recursion, mem concerns, integer overflow - non-issue b/c practical solvability degrades fast

    public static void main(String[] args) {
        final Stopwatch clock = Stopwatch.createStarted();
        // -- solve --
        final SpecializedSet<Integer> soln
                = solve(20, SpecializedSet.empty());
        clock.stop();
        System.out.println(
            soln == null ? "<no solution>"
                : Pretty.fromBoard(soln.asSet()));
        System.out.println(clock);
    }

    @Nullable static SpecializedSet<Integer> solve(int N, SpecializedSet<Integer> acc) {
        if (acc.size() == N)
            return acc;
        for (int row = 0; row < N; ++row) {
            if (isSafeAddition(acc, row)) {
                final SpecializedSet<Integer> soln = solve(N, acc.add(row)/*O(1)*/);
                if (soln != null)
                    return soln;
            }
        }
        return null;
    }

    private static boolean isSafeAddition(SpecializedSet<Integer> acc, int row) {
        return !acc.contains(row) // O(1)
            && every(acc, idx -> val -> abs(acc.size() - idx) != abs(row - val)); // O(|acc|)
    }

    private static boolean isSafeAddition_(SpecializedSet<Integer> acc, int row) {
        return isSafeAddition(acc, row)
            && (acc.size() < 2 ||
                Math.combinations(acc.size(), 2)
                    /*O(|acc|)*/.noneMatch(comb ->
                        Math.isColinear(comb[0], acc.get(comb[0]), comb[1],
                            acc.get(comb[1]), acc.size(), row)));
    }

    /** Note: indexed forAll not available in vavr; consider parallel version. */
    private static <T> boolean every(SpecializedSet<T> acc,
                                     Function<Integer, Function<T, Boolean>> pred) {
        int idx = 0;
        for (T t : acc.asSet())
            if (!pred.apply(idx++).apply(t))
                return false;
        return true;
    }

}
