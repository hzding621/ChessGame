package chessgame.board;

import java.util.Optional;

/**
 * Immutable class that represents a single cell on a two dimensional chess board
 */
public class SquareCell implements Cell {

    private final File file;
    private final Rank rank;

    private SquareCell(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    public static SquareDirection findDirection(SquareCell startCell, SquareCell endCell) {
        if (startCell.equals(endCell)) {
            throw new IllegalArgumentException("Two cells are equal!");
        }
        int x1 = startCell.getFile().getCoordinate().getIndex(), x2 = endCell.getFile().getCoordinate().getIndex();
        int y1 = startCell.getRank().getCoordinate().getIndex(), y2 = endCell.getRank().getCoordinate().getIndex();
        int xDiff = x2 - x1, yDiff = y2 - y1;
        if (x1 == x2) {
            return yDiff > 0 ? SquareDirection.NORTH : SquareDirection.SOUTH;
        } else if (y1 == y2) {
            return xDiff > 0 ? SquareDirection.EAST : SquareDirection.WEST;
        } else {
            if (Math.abs(xDiff) != Math.abs(yDiff)) {
                throw new IllegalArgumentException("Two cells are not on the same line!");
            }
            if (xDiff > 0 && yDiff > 0) return SquareDirection.NORTHEAST;
            if (xDiff > 0 && yDiff < 0) return SquareDirection.SOUTHEAST;
            if (xDiff < 0 && yDiff > 0) return SquareDirection.NORTHWEST;
            if (xDiff < 0 && yDiff < 0) return SquareDirection.SOUTHWEST;
        }
        throw new IllegalArgumentException("Should never reach here!");
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }

    @Override
    public int compareTo(Cell o) {
        if (!(o instanceof SquareCell)) {
            throw new IllegalStateException("Cannot compare SquareCell to a cell of different type: "
                    + o.getClass() + "! ");
        }
        SquareCell that = (SquareCell) o;
        int fileDiff = this.file.compareTo(that.file);
        if (fileDiff != 0) return fileDiff;
        return this.rank.compareTo(that.rank);
    }

    public static final class Builder implements GridCellFactory<SquareCell, SquareDirection> {
        private final Coordinate.Builder rankBuilder;
        private final Coordinate.Builder fileBuilder;

        public Builder(Coordinate.Builder fileBuilder, Coordinate.Builder rankBuilder) {
            this.fileBuilder = fileBuilder;
            this.rankBuilder = rankBuilder;
        }

        @Override
        public SquareCell at(String file, String rank) {
            if (file.length() != 1) {
                throw new IllegalArgumentException("Grid cell factory does not support encoding of files larger than 26!");
            }
            if (file.charAt(0) > 'Z' || file.charAt(0) < 'A') {
                throw new IllegalArgumentException("Grid cell factory only support uppercase english encoding for files!");
            }
            int fileIndex = file.charAt(0) - 'A';
            int rankIndex = Integer.parseInt(rank) - 1;
            return at(fileIndex, rankIndex);
        }

        @Override
        public SquareCell at(int fileIndex, int rankIndex) {
            Coordinate fileCoordinate = fileBuilder.at(fileIndex);
            Coordinate rankCoordinate = rankBuilder.at(rankIndex);
            return new SquareCell(File.of(fileCoordinate), Rank.of(rankCoordinate));
        }

        @Override
        public Optional<SquareCell> moveOnce(SquareCell cell, SquareDirection direction) {
            int xDelta = direction.getX(), yDelta = direction.getY();
            int fileIndex = cell.file.getCoordinate().getIndex() + xDelta;
            int rankIndex = cell.rank.getCoordinate().getIndex() + yDelta;
            if (!withinRange(fileIndex, rankIndex)) {
                return Optional.empty();
            }
            return Optional.of(at(fileIndex, rankIndex));
        }

        @Override
        public boolean withinRange(int fileIndex, int rankIndex) {
            return rankBuilder.withinRange(rankIndex) && fileBuilder.withinRange(fileIndex);
        }

        @Override
        public boolean withinRange(String file, String rank) {
            if (file.length() != 1 || file.charAt(0) > 'Z' || file.charAt(0) < 'A') {
                return false;
            }
            int fileIndex = file.charAt(0) - 'A';
            int rankIndex = Integer.parseInt(rank) - 1;
            return fileBuilder.withinRange(fileIndex) && rankBuilder.withinRange(rankIndex);
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SquareCell that = (SquareCell) o;

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