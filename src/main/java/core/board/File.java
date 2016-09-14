package core.board;

/**
 * Wrapper class of coordinate that represents a file in standard chess game with toString() override
 */
public final class File implements Comparable<File> {

    private final Coordinate delegate;

    private File(Coordinate coordinate) {
        this.delegate = coordinate;
    }

    public static File of(Coordinate coordinate) {
        return new File(coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return delegate.equals(file.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf((char) (delegate.getIndex() + 'A'));
    }

    public Coordinate getCoordinate() {
        return delegate;
    }

    @Override
    public int compareTo(File o) {
        if (this == o) return 0;
        return delegate.compareTo(o.delegate);
    }
}