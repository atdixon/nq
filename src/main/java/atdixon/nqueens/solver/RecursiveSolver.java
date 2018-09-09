package atdixon.nqueens.solver;

import javax.annotation.Nullable;

import static atdixon.nqueens.solver.Math.combinations;
import static atdixon.nqueens.solver.Math.isColinear;
import static java.lang.Math.abs;

/**
 * Recursive DFS solver.
 */
public final class RecursiveSolver {

    private RecursiveSolver() {}

    @Nullable public static Board solve(int N) {
        return solve(Board.empty(N));
    }

    @Nullable private static Board solve(Board acc) {
        if (acc.numQueens() == acc.width())
            return acc;
        for (int row = 0; row < acc.width(); ++row) {
            if (isSafeAddition(acc, acc.numQueens(), row)) {
                final Board soln = solve(acc.addQueen(acc.numQueens(), row)/*O(1)*/);
                if (soln != null)
                    return soln;
            }
        }
        return null;
    }

    private static boolean isSafeAdditionStandard(Board acc, int col, int row) {
        return acc.numQueensInRow(row) == 0 // O(1)
            && acc.queenRowsForColumnRange(0, col)
                .zipWithIndex()
                .forAll(tpl -> abs(col - tpl._2) != abs(row - tpl._1)); // O(|acc|)
    }

    private static boolean isSafeAddition(Board acc, int col, int row) {
        return isSafeAdditionStandard(acc, col, row)
            && (col < 2 ||
                combinations(col, 2)
                    /*O(|acc|^2)*/.noneMatch(comb ->
                        isColinear(comb.get(0), acc.queenRowForColumn(comb.get(0)), comb.get(1),
                            acc.queenRowForColumn(comb.get(1)), col, row)));
    }

}
