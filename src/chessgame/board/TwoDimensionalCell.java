package chessgame.board;

import java.util.Optional;

/**
 * Immutable class that represents a single cell on a two dimensional chess board
 */
public class TwoDimensionalCell implements Cell {

    private final File file;
    private final Rank rank;

    private TwoDimensionalCell(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }

    public static final class Factory {
        private final Coordinate.Factory rankFactory;
        private final Coordinate.Factory fileFactory;

        public Factory(Coordinate.Factory rankFactory, Coordinate.Factory fileFactory) {
            this.rankFactory = rankFactory;
            this.fileFactory = fileFactory;
        }

        public Optional<TwoDimensionalCell> of(int fileIndex, int rankIndex) {
            Optional<Coordinate> fileCoordinate = fileFactory.of(fileIndex);
            Optional<Coordinate> rankCoordinate = rankFactory.of(rankIndex);

            if (!fileCoordinate.isPresent() || !rankCoordinate.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(new TwoDimensionalCell(File.of(fileCoordinate.get()), Rank.of(rankCoordinate.get())));
        }

        public Optional<TwoDimensionalCell> moveOnce(TwoDimensionalCell cell, TwoDimensionalDirection direction) {
            int xDelta = direction.getX(), yDelta = direction.getY();
            int fileIndex = cell.file.getCoordinate().getIndex() + xDelta;
            int rankIndex = cell.rank.getCoordinate().getIndex() + yDelta;
            return of(fileIndex, rankIndex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoDimensionalCell that = (TwoDimensionalCell) o;

        if (!rank.equals(that.rank)) return false;
        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        int result = rank.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }
}