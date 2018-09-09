package atdixon.nqueens.impl;

import javax.annotation.Nullable;
import java.util.function.Function;

import static java.lang.Math.abs;

public final class Solver {

    private Solver() {}

    @Nullable public static SpecializedSet<Integer> solve(int N, SpecializedSet<Integer> acc) {
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
                    /*O(|acc|^2)*/.noneMatch(comb ->
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
