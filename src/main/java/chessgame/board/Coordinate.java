package chessgame.board;

import utility.LoggerFactory;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Represents a coordinate in an abstract chess board and provides utility class to mutate the coordinate with boundary checks
 */

public final class Coordinate implements Comparable<Coordinate> {

    private static final Logger log = LoggerFactory.getInstance(Coordinate.class);

    private final int index;

    private Coordinate(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static class Factory {

        private final int length;
        public Factory(int length) {
            this.length = length;
        }

        public Optional<Coordinate> of(int index) {
            if (index < 0 || index >= length) {
                return Optional.empty();
            }
            return Optional.of(new Coordinate(index));
        }

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate coordinate = (Coordinate) o;
        return index == coordinate.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public int compareTo(Coordinate o) {
        if (this == o) return 0;
        return this.index - o.index;
    }
}