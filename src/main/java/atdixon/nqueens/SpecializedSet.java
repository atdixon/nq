package atdixon.nqueens;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.Set;

import javax.annotation.Nullable;

/** Immutable set with these properties: ...with all operations constant-time. */
final class SpecializedSet<T> {

    static <T> SpecializedSet<T> empty() {
        return new SpecializedSet<>(LinkedHashSet.empty(), LinkedHashMap.empty());
    }

    private final LinkedHashSet<T> set;
    private final LinkedHashMap<Integer, T> indexed;

    private SpecializedSet(LinkedHashSet<T> set,
                           LinkedHashMap<Integer, T> indexed) {
        this.set = set;
        this.indexed = indexed;
    }

    public SpecializedSet<T> add(T val) {
        return set.contains(val) ? this :
            new SpecializedSet<>(set.add(val), indexed.put(indexed.size(), val));
    }

    public int size() {
        return set.size();
    }

    public boolean contains(T val) {
        return set.contains(val);
    }

    @Nullable public T get(int index) {
        return indexed.getOrElse(index, null);
    }

    public Set<T> asSet() {
        return set;
    }

}
