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

/**
 * This chessboard representation is essentially a linked, <em>immutable</em>,
 * <em>persistent</em> set with constant-time operations including constant-time
 * index lookup.
 *
 * An important restriction is that while some columns may have no placed queens,
 * no column contains more than one placed queen. */
public final class Board {

    /** Create an empty (no placed queens) board with the given <code>width</code> dimension. */
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

    /** Dimensional size of board. */
    public int width() {
        return width;
    }

    /** All columns have a placed queen. */
    public boolean isFull() {
        return width() == numQueens();
    }

    /**
     * Add queens to board at the specified rows.
     *
     * Precondition: no queens yet exist in the columns specified by
     * the indices of the provided list.
     */
    public Board addQueens(List<Integer> rows) {
        return rows.zipWithIndex()
            .foldLeft(this,
                (acc, tpl) -> acc.addQueen(tpl._2, tpl._1));
    }

    /**
     * Precondition: no queen is yet placed at the provided column
     * <code>col</code>.
     */
    public Board addQueen(int col, int row) {
        checkState(col < width && row < width);
        checkState(!colToRow.containsKey(col));
        return new Board(width,
            rowToCol.put(row, col), colToRow.put(col, row));
    }

    /** Move queen at column <code>code</code> to row <code>row</code>. */
    public Board moveQueen(int col, int row) {
        checkState(col < width && row < width);
        return new Board(width,
            (colToRow.containsKey(col)
                ? rowToCol.remove(colToRow.get(col).get(), col)
                : rowToCol).put(row, col),
            colToRow.put(col, row));
    }

    /** Number of queens placed on the board. */
    public int numQueens() {
        return colToRow.size();
    }

    /** Number of queens placed on the given row. */
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

    /** Columns of all queens placed on the given row. */
    public Set<Integer> queenColumnsForRow(int row) {
        return LinkedHashSet.ofAll(rowToCol.get(row).getOrElse(List.empty()));
    }

}
