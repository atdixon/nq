package atdixon.nqueens.support;

import java.util.function.Function;

public final class Util {

    private Util() {}

    public static <T> boolean forAllWithIndex(Iterable<T> coll,
                                              Function<Integer, Function<T, Boolean>> pred) {
        int idx = 0;
        for (T t : coll)
            if (!pred.apply(idx++).apply(t))
                return false;
        return true;
    }

}
