package atdixon.nqueens;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

final class Pretty {

    private Pretty() {}

    static String fromBoard(Iterable<Integer> solution) {
        final Map<Integer, Integer> transpose = transpose(solution);
        final int N = transpose.size();
        return range(0, N)
            .map(i -> N - i - 1) // ...to row index
            .mapToObj(row -> fromRow(N, row, transpose.get(row)))
            .collect(joining("\n"))
            + "\n" + range(0, N)
                .mapToObj(i -> String.valueOf((char) ('a' + i)))
                .collect(joining(" "));
    }

    private static String fromRow(int N, int row, int column) {
        return new StringBuilder()
            .append(Strings.repeat("* ", column))
            .append("Q ")
            .append(Strings.repeat("* ", N - column - 1))
            .append(row + 1) // chessboard numerals are 1-based
            .toString();
    }

    private static Map<Integer, Integer> transpose(Iterable<Integer> coll) {
        final Map<Integer, Integer> transpose = new HashMap<>();
        int col = 0;
        for (Integer val : coll)
            transpose.put(val, col++);
        return transpose;
    }

}
