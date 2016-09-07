package chessgame.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents a coordinate in an abstract chess board and provides utility class to mutate the coordinate with boundary checks
 */

public final class Coordinate implements Comparable<Coordinate> {

    private static final Logger log = LoggerFactory.getLogger(Coordinate.class);

    private final int index;

    private Coordinate(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static class Builder {

        private final int length;
        public Builder(int length) {
            this.length = length;
        }

        public Coordinate at(int index) {
            if (!withinRange(index)) {
                throw new IllegalStateException("Coordinate index out of range!");
            }
            return new Coordinate(index);
        }

        public boolean withinRange(int index) {
            return index >= 0 && index < length;
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
