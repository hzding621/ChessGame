package utility;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Utility methods
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Utils
    }

    /**
     * @return the last element in the list, or empty if the list is empty
     */
    public static <E> Optional<E> last(List<E> list) {
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(list.size() - 1));
    }

    /**
     * @return transform the element into an arraylist containing the element, or an empty arraylist if the element is empty
     */
    @SafeVarargs
    public static <E> List<E> asArrayList(Optional<E>... elements) {
        List<E> newArrayList = new ArrayList<>();
        for (Optional<E> element: elements) {
            element.ifPresent(newArrayList::add);
        }
        return newArrayList;
    }

    public static <E> Collector<Optional<E>, Collection<E>, Stream<E>> filterEmpty() {
        return Collector.of(ConcurrentLinkedQueue::new,
                (q, oe) -> oe.ifPresent(q::add),
                (q1, q2) -> {q1.addAll(q2); return q1;},
                Collection::stream);
    }
}
