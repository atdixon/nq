package atdixon.nqueens;

import atdixon.nqueens.impl.Solver;
import atdixon.nqueens.impl.SpecializedSet;
import com.google.common.base.Stopwatch;

public final class Cli {

    // discuss: why immutable/persistent! LinkedHashSet
    // guava vs vavr
    // recursion, mem concerns, integer overflow - non-issue b/c practical solvability degrades fast
    // fractional math etc.

    public static void main(String[] args) {
        // -- args --
        final int N = Integer.parseInt(args[0]);
        // -- solve --
        final Stopwatch clock = Stopwatch.createStarted();
        final SpecializedSet<Integer> soln
            = Solver.solve(N, SpecializedSet.empty());
        clock.stop();
        // -- output --
        System.out.println(
            soln == null ? "<no solution>"
                : Pretty.fromBoard(soln.asSet()));
        System.out.println(clock);
    }

}
