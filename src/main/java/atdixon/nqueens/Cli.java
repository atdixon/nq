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
        final boolean useHeuristicSolver = args.length > 1
            && "heuristic".equals(args[1]);
        // -- solve --
        final Stopwatch clock = Stopwatch.createStarted();
        final Board soln =
            useHeuristicSolver
                ? HeuristicSolver.solve(N)
                : RecursiveSolver.solve(N);
        clock.stop();
        // -- output --
        System.out.println("solver: " +
            (useHeuristicSolver ? "heuristic" : "default"));
        System.out.println(soln == null
            ? "no solution found" : Pretty.of(soln));
        System.out.println(clock);
    }

    private static String usage() {
        return "Usage: nq <N> [default|heuristic]";
    }

}
