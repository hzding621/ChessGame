package utility;

import java.util.*;

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
}
