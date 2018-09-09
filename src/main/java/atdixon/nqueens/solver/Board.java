package atdixon.nqueens.solver;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.LinkedHashMultimap;
import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;

/** Immutable set with these properties: ...with all operations constant-time. */
public final class Board {

    public static Board empty(int width) {
        return new Board(width,
            LinkedHashMultimap.withSet().empty(),
            LinkedHashMap.empty());
    }

    private final int width;
    private final LinkedHashMultimap<Integer, Integer> rowToCol;
    private final LinkedHashMap<Integer, Integer> colToRow;

    private Board(int width,
                  LinkedHashMultimap<Integer, Integer> rowToCol,
                  LinkedHashMap<Integer, Integer> colToRow) {
        this.width = width;
        this.rowToCol = rowToCol;
        this.colToRow = colToRow;
    }

    public int width() {
        return width;
    }

    public boolean isFull() {
        return width() == numQueens();
    }

    public Board addQueens(List<Integer> rows) {
        return rows.zipWithIndex()
            .foldLeft(this,
                (acc, tpl) -> acc.addQueen(tpl._2, tpl._1));
    }

    /** Precondition: queen does not yet exist in <code>col</code>. */
    public Board addQueen(int col, int row) {
        checkState(col < width && row < width);
        checkState(!colToRow.containsKey(col));
        return new Board(width,
            rowToCol.put(row, col), colToRow.put(col, row));
    }

    public Board moveQueen(int col, int row) {
        checkState(col < width && row < width);
        return new Board(width,
            (colToRow.containsKey(col)
                ? rowToCol.remove(colToRow.get(col).get(), col)
                : rowToCol).put(row, col),
            colToRow.put(col, row));
    }

    public int numQueens() {
        return colToRow.size();
    }

    public int numQueensInRow(int row) {
        return rowToCol.get(row)
            .map(Traversable::size)
            .getOrElse(0);
    }

    /** Answers null or the row of the queen in the provided column. */
    @Nullable public Integer queenRowForColumn(int col) {
        return colToRow.getOrElse(col, null);
    }

    /** Answer all columns; for non-full boards, may contain null values;
     * May contain duplicate values. */
    public List<Integer> queenRows() {
        return queenRowsForColumnRange(0, width);
    }

    /** Note: List will contains nulls for empty cols; may contain duplicate values
     * (e.g., for different queens on same row). */
    public List<Integer> queenRowsForColumnRange(int begin, int endExclusive) {
        return IntStream.range(begin, endExclusive)
            .mapToObj(this::queenRowForColumn)
            .collect(List.collector());
    }

    public Set<Integer> queenColumnsForRow(int row) {
        return LinkedHashSet.ofAll(rowToCol.get(row).getOrElse(List.empty()));
    }

}
