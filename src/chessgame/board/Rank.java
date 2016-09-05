package chessgame.board;

/**
 * Wrapper class of coordinate that represents a rank in standard chess game with toString() override
 */

public final class Rank implements Comparable<Rank> {
    private final Coordinate delegate;

    private Rank(Coordinate coordinate) {
        this.delegate = coordinate;
    }

    public static Rank of(Coordinate coordinate) {
        return new Rank(coordinate);
    }

    public Coordinate getCoordinate() {
        return delegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rank rank = (Rank) o;

        return delegate.equals(rank.delegate);

    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(delegate.getIndex() + 1);
    }

    @Override
    public int compareTo(Rank o) {
        if (this == o) return 0;
        return delegate.compareTo(o.delegate);
    }
}