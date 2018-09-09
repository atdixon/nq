package atdixon.nqueens.solver;

import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.Random;

import static atdixon.nqueens.support.Math.combinations;
import static atdixon.nqueens.support.Math.isColinear;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.abs;

public final class HeuristicSolver {

    private static final Random RANDOM = new Random();

    private HeuristicSolver() {}

    public static Board solve(int N) {
        // -- initial state (randomized) --
        Board curr = Board.empty(N)
            .addQueens(List.range(0, N).shuffle());
        // -- "seen" candidates --
        Set<List<Integer>> seen
            = HashSet.of(curr.queenRows(), curr.queenRows().reverse());
        // -- search loop --
        for (;;) {
            boolean hasThreats = false;
            Board next = null;
            // -- find unsafe queen (starting from random col) --
            final int rand = RANDOM.nextInt(N);
            for (int i = 0; i < N && next == null; ++i) {
                final int col = (i + rand) % N;
                if (distance_(curr, col) > 0) {
                    hasThreats = true;
                    final Board curr_ = curr;
                    final Set<List<Integer>> seen_ = seen;
                    // -- find next move (ie, minimize threats) --
                    next = List.range(0, N)
                        // do not consider current row
                        .filter(row ->
                            !row.equals(checkNotNull(curr_.queenRowForColumn(col))))
                        // determine next candidate and weight (ie, num threats)
                        .map(row -> {
                            final Board candidate = curr_.moveQueen(col, row);
                            return new Tuple2<>(candidate, distance_(candidate, col)); })
                        // do not consider already-seen
                        .filter(tpl -> !seen_.contains(tpl._1.queenRows()))
                        // choose minimum weight
                        .minBy(tpl -> tpl._2).map(tpl -> tpl._1)
                        .getOrNull();
                }
            }
            if (!hasThreats)
                return curr; // success
            if (next == null)
                return null; // failure
            seen = seen.add(next.queenRows())
                .add(curr.queenRows().reverse());
            curr = next;
        }
    }

    /** "Distance function"; heuristic "cost" of queen in given column.
     * Non-zero iff lone queen is safe wrt board rules. */
    private static int distance(Board board, int col) {
        return countThreats(board, col);
    }

    /** "Distance function"; heuristic "cost" of queen in given column. */
    private static int distance_(Board board, int col) {
        return countThreats(board, col) + countColinearPairs(board, col);
    }

    /** Precondition: {@link Board#isFull()}. */
    private static int countThreats(Board board, int col) { // O(n)
        checkState(board.isFull());
        final int row = checkNotNull(board.queenRowForColumn(col));
        return board.queenRows()
            .zipWithIndex()
            .filter(tpl -> tpl._2 != col || tpl._1 != row)
            .count(tpl ->
                 tpl._1 == row
                    || abs(tpl._2 - col) == abs(tpl._1 - row));
    }

    /** Precondition: {@link Board#isFull()}. */
    private static int countColinearPairs(Board board, int col) { // O(n^2)
        final int row = checkNotNull(board.queenRowForColumn(col));
        return (int) combinations(board.width() - 1, 2)
            .map(comb -> List.of(
                comb.get(0) >= col ? comb.get(0) + 1 : comb.get(0),
                comb.get(1) >= col ? comb.get(1) + 1 : comb.get(1)))
            .filter(comb -> {
                final int x1 = comb.get(0), y1 = checkNotNull(board.queenRowForColumn(x1));
                final int x2 = comb.get(1), y2 = checkNotNull(board.queenRowForColumn(x2));
                return isColinear(x1, y1, x2, y2, col, row);
            }).count();
    }

}
