package atdixon.nqueens.solver;

import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import org.apache.commons.math3.fraction.Fraction;

import javax.annotation.Nullable;

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
            && !isColinearAddition(acc, col, row);
    }

    private static boolean isColinearAddition(Board board, int col, int row) { // O(|board|)
        return board.queenRowsForColumnRange(0, col)
            .zipWithIndex()
            .foldLeft(new Tuple2<>(HashSet.empty(), false),
                (acc, qr) -> {
                    final Fraction slope = new Fraction(col - qr._2, row - qr._1);
                    return new Tuple2<>(
                        acc._1.add(slope),
                        acc._2 || acc._1.contains(slope)); })._2;
    }

}
