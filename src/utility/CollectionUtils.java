package utility;

import java.util.*;

/**
 * Utility methods
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Utils
    }

    public static <E> Optional<E> last(List<E> list) {
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(list.size() - 1));
    }

    public static <E> List<E> asArrayList(Optional<E> element) {
        List<E> newArrayList = new ArrayList<>();
        element.ifPresent(newArrayList::add);
        return newArrayList;
    }

}
