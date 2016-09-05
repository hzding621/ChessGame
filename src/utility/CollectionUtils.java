package utility;

import java.util.List;
import java.util.Optional;

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

}
