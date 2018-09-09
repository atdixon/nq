package atdixon.nqueens;

import atdixon.nqueens.solver.Board;
import atdixon.nqueens.solver.HeuristicSolver;
import atdixon.nqueens.solver.RecursiveSolver;
import com.google.common.base.Stopwatch;

public final class Cli {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(usage());
            return;
        }
        // -- parse args --
        final int N = Integer.parseInt(args[0]);
        final String type
            = args.length < 2 ? "random" : args[1];
        // -- solve --
        final Stopwatch clock = Stopwatch.createStarted();
        final Board soln =
            type.equals("random")
                ? HeuristicSolver.solve(N)
                : RecursiveSolver.solve(N);
        clock.stop();
        // -- output --
        System.out.println(soln == null
            ? "no solution found" : Pretty.of(soln));
        System.out.println(clock);
    }

    private static String usage() {
        return "Usage: command <N> [random|dfs]";
    }

}
