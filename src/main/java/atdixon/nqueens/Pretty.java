package atdixon.nqueens;

import atdixon.nqueens.solver.Board;
import io.vavr.collection.Set;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public final class Pretty {

    private Pretty() {}

    public static String of(Board board) {
        return range(0, board.width())
            .map(i -> board.width() - i - 1) // ...to row index
            .mapToObj(row -> ofRow(board, row))
            .collect(joining("\n"))
            + "\n" + range(0, board.width())
                .mapToObj(i -> String.valueOf((char) ('a' + i)))
                .collect(joining(" "));
    }

    private static String ofRow(Board board, int row) {
        final Set<Integer> cols = board.queenColumnsForRow(row);
        return IntStream.rangeClosed(0, board.width())
            .mapToObj(i -> i == board.width() ? String.valueOf(row + 1)
                : cols.contains(i) ? "Q" : "*")
            .collect(joining(" "));
    }

}
