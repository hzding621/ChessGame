/**
 * Immutable class that represents a single cell on a 8x8 chess board
 */
public final class SquareBoardCell implements Location {
    private final Rank rank;
    private final File file;

    private SquareBoardCell(Rank rank, File file) {
        this.rank = rank;
        this.file = file;
    }

    public static SquareBoardCell of(Rank rank, File file) {
        return new SquareBoardCell(rank, file);
    }

    public Rank getRank() {
        return rank;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SquareBoardCell squareBoardCell = (SquareBoardCell) o;

        if (rank != squareBoardCell.rank) return false;
        return file == squareBoardCell.file;
    }

    @Override
    public int hashCode() {
        int result = rank.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }

    public enum Rank {
        ONE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7);

        private final int index;

        Rank(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return String.valueOf(index + 1);
        }

        public int getIndex() {
            return index;
        }
    }

    public enum File {
        A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

        private final int index;

        File(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return String.valueOf(index + 1);
        }

        public int getIndex() {
            return index;
        }
    }
}
