package atdixon.nqueens.solver;

import atdixon.nqueens.support.Util;

import javax.annotation.Nullable;

import static atdixon.nqueens.support.Math.combinations;
import static atdixon.nqueens.support.Math.isColinear;
import static java.lang.Math.abs;

public final class RecursiveSolver {

    private RecursiveSolver() {}

    @Nullable public static Board solve(int N) {
        return solve(Board.empty(N));
    }

    @Nullable private static Board solve(Board acc) {
        if (acc.numQueens() == acc.width())
            return acc;
        for (int row = 0; row < acc.width(); ++row) {
            if (isSafeAddition_(acc, acc.numQueens(), row)) {
                final Board soln = solve(acc.addQueen(acc.numQueens(), row)/*O(1)*/);
                if (soln != null)
                    return soln;
            }
        }
        return null;
    }

    private static boolean isSafeAddition(Board acc, int col, int row) {
        return acc.numQueensInRow(row) == 0 // O(1)
            && Util.forAllWithIndex(acc.queenRowsForColumnRange(0, col),
                idx -> val -> abs(col - idx) != abs(row - val)); // O(|acc|)
    }

    private static boolean isSafeAddition_(Board acc, int col, int row) {
        return isSafeAddition(acc, col, row)
            && (col < 2 ||
                combinations(col, 2)
                    /*O(|acc|^2)*/.noneMatch(comb ->
                        isColinear(comb.get(0), acc.queenRowForColumn(comb.get(0)), comb.get(1),
                            acc.queenRowForColumn(comb.get(1)), col, row)));
    }

}
