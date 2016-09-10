package chessgame.board;

import java.util.Optional;

/**
 * Immutable class that represents a single cell on a two dimensional chess board
 */
public final class Square implements Cell {

    private final File file;
    private final Rank rank;

    private Square(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    public static TwoDimension findDirection(Square startCell, Square endCell) {
        if (startCell.equals(endCell)) {
            throw new IllegalArgumentException("Two cells are equal!");
        }
        int x1 = startCell.getFile().getCoordinate().getIndex(), x2 = endCell.getFile().getCoordinate().getIndex();
        int y1 = startCell.getRank().getCoordinate().getIndex(), y2 = endCell.getRank().getCoordinate().getIndex();
        int xDiff = x2 - x1, yDiff = y2 - y1;
        if (x1 == x2) {
            return yDiff > 0 ? TwoDimension.NORTH : TwoDimension.SOUTH;
        } else if (y1 == y2) {
            return xDiff > 0 ? TwoDimension.EAST : TwoDimension.WEST;
        } else {
            if (Math.abs(xDiff) != Math.abs(yDiff)) {
                throw new IllegalArgumentException("Two cells are not on the same line!");
            }
            if (xDiff > 0 && yDiff > 0) return TwoDimension.NORTHEAST;
            if (xDiff > 0 && yDiff < 0) return TwoDimension.SOUTHEAST;
            if (xDiff < 0 && yDiff > 0) return TwoDimension.NORTHWEST;
            if (xDiff < 0 && yDiff < 0) return TwoDimension.SOUTHWEST;
        }
        throw new IllegalArgumentException("Should never reach here!");
    }

    public Builder builder(int fileLength, int rankLength) {
        Coordinate.Builder fileBuilder = new Coordinate.Builder(fileLength);
        Coordinate.Builder rankBuilder = new Coordinate.Builder(rankLength);
        return new Builder(fileBuilder, rankBuilder);
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }

    @Override
    public int compareTo(Cell o) {
        if (!(o instanceof Square)) {
            throw new IllegalStateException("Cannot compare Square to a cell of different type: "
                    + o.getClass() + "! ");
        }
        Square that = (Square) o;
        int fileDiff = this.file.compareTo(that.file);
        if (fileDiff != 0) return fileDiff;
        return this.rank.compareTo(that.rank);
    }

    public static final class Builder implements GridCellBuilder<Square, TwoDimension> {
        private final Coordinate.Builder rankBuilder;
        private final Coordinate.Builder fileBuilder;

        public Builder(Coordinate.Builder fileBuilder, Coordinate.Builder rankBuilder) {
            this.fileBuilder = fileBuilder;
            this.rankBuilder = rankBuilder;
        }

        @Override
        public Square at(String file, String rank) {
            if (file.length() != 1) {
                throw new IllegalArgumentException("Grid cell factory does not support encoding of files larger than 26!");
            }
            if (!Character.isAlphabetic(file.charAt(0))) {
                throw new IllegalArgumentException("Grid cell factory only support alphabetic encoding for files!");
            }

            int fileIndex = Character.toUpperCase(file.charAt(0)) - 'A';
            int rankIndex = Integer.parseInt(rank) - 1;
            return at(fileIndex, rankIndex);
        }

        @Override
        public Square at(int fileIndex, int rankIndex) {
            Coordinate fileCoordinate = fileBuilder.at(fileIndex);
            Coordinate rankCoordinate = rankBuilder.at(rankIndex);
            return new Square(File.of(fileCoordinate), Rank.of(rankCoordinate));
        }

        @Override
        public Optional<Square> moveOnce(Square cell, TwoDimension direction, Projection projection) {

            if (projection.getForward() < 0) {
                direction = direction.reverse();
            }
            int fileIndex = cell.file.getCoordinate().getIndex();
            int rankIndex = cell.rank.getCoordinate().getIndex();
            int xDelta = direction.getX(), yDelta = direction.getY();
            for (int i = 0; i < Math.abs(projection.getForward()); i++) {
                fileIndex += xDelta;
                rankIndex += yDelta;
                if (!withinRange(fileIndex, rankIndex)) {
                    return Optional.empty();
                }
            }
            if (projection.getRotate() > 0) {
                direction = direction.nextClockWise().nextClockWise();
            } else {
                direction = direction.nextCounterClockWise().nextCounterClockWise();
            }
            xDelta = direction.getX(); yDelta = direction.getY();
            for (int j = 0; j < Math.abs(projection.getRotate()); j++) {
                fileIndex += xDelta;
                rankIndex += yDelta;
                if (!withinRange(fileIndex, rankIndex)) {
                    return Optional.empty();
                }
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

        Square that = (Square) o;

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