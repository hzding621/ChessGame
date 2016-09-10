package chessgame.board;

import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.game.GameSetting;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import utility.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an abstract rectangular board
 */
public abstract class RectangularBoard<P extends PieceClass, V extends GridViewer<Square, TwoDimension, P>, T extends RectangularBoard<P, V, T>>
        extends AbstractBoard<Square, P, V, T> implements GridViewer<Square, TwoDimension, P> {

    public static class Instance<P extends PieceClass> extends RectangularBoard<P, Instance<P>, Instance<P>> {

        private Instance(Map<Square, Piece<P>> occupants, Square.Builder cellBuilder) {
            super(occupants, cellBuilder);
        }

        public static <P extends PieceClass> RectangularBoard.Instance<P> create(
                GameSetting.GridGame<Square, P> gameSetting) {
            final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
            final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
            return new RectangularBoard.Instance<>(gameSetting.constructPiecesByStartingPosition(),
                    new Square.Builder(fileBuilder, rankBuilder));
        }

        @Override
        public TransitionResult<Square, P> apply(BoardTransition<Square, P, RectangularBoard.Instance<P>> boardTransition) {
            return boardTransition.apply(this);
        }

        @Override
        public RectangularBoard.Instance<P> preview(BoardTransition<Square, P, RectangularBoard.Instance<P>> transition) {
            RectangularBoard.Instance<P> newInstance = new RectangularBoard.Instance<P>(occupants, cellBuilder);
            newInstance.apply(transition);
            return newInstance;
        }
    }

    protected final Square.Builder cellBuilder;

    protected RectangularBoard(Map<Square, Piece<P>> occupants, Square.Builder cellBuilder) {
        super(occupants);
        this.cellBuilder = cellBuilder;
    }

    @Override
    public boolean isOccupied(Square cell) {
        validatePosition(cell);
        return super.isOccupied(cell);
    }

    @Override
    public boolean isEnemy(Square cell, Player player) {
        validatePosition(cell);
        return super.isEnemy(cell, player);
    }

    @Override
    public Optional<Piece<P>> getPiece(Square cell) {
        validatePosition(cell);
        return super.getPiece(cell);
    }

    @Override
    public Piece<P> movePiece(Square source, Square target) {
        validatePosition(source);
        validatePosition(target);
        return super.movePiece(source, target);
    }

    @Override
    public Piece<P> clearPiece(Square position) {
        validatePosition(position);
        return super.clearPiece(position);
    }

    @Override
    public void addPiece(Square position, Piece<P> piece) {
        validatePosition(position);
        super.addPiece(position, piece);
    }

    @Override
    public Collection<TwoDimension> getAllDirections() {
        return Arrays.asList(TwoDimension.VALUES);
    }

    @Override
    public Collection<TwoDimension> getOrthogonalDirections() {
        return Arrays.asList(TwoDimension.ORTHOGONALS);
    }

    @Override
    public Collection<TwoDimension> getDiagonalDirections() {
        return Arrays.asList(TwoDimension.DIAGONALS);
    }


    @Override
    public TwoDimension findDirection(Square startCell, Square endCell) {
        validatePosition(startCell);
        validatePosition(endCell);
        return Square.findDirection(startCell, endCell);
    }

    @Override
    public Optional<Square> moveSteps(Square startCell, TwoDimension direction, int steps, Distance distance) {
        validatePosition(startCell);
        Optional<Square> cell = Optional.of(startCell);
        while (steps > 0 && cell.isPresent()) {
            cell =  getGridCellFactory().moveOnce(cell.get(), direction, distance);
            steps--;
        }
        return cell;
    }

    @Override
    public List<Square> furthestReach(Square startCell,
                                      TwoDimension direction,
                                      Distance distance,
                                      boolean startInclusive,
                                      boolean meetInclusive) {
        validatePosition(startCell);
        List<Square> cellList = new ArrayList<>();
        if (startInclusive) {
            cellList.add(startCell);
        }
        Optional<Square> nextCell = moveSteps(startCell, direction, 1, distance);
        Optional<Piece<P>> piece = Optional.empty();
        while (nextCell.isPresent()) {
            piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant
                break;
            }
            cellList.add(nextCell.get());
            nextCell = moveSteps(nextCell.get(), direction, 1, distance);
        }
        if (meetInclusive && piece.isPresent()) {
            cellList.add(nextCell.get());
        }
        return cellList;
    }

    public Optional<Square> firstOccupant(Square startCell, TwoDimension direction, Distance distance) {
        validatePosition(startCell);
        return CollectionUtils.last(furthestReach(startCell, direction, distance, false, true)).filter(this::isOccupied);
    }

    @Override
    public Optional<Square> moveForward(Square startCell, Player player) {
        validatePosition(startCell);
        if (player == Player.WHITE) {
            return getGridCellFactory().moveOnce(startCell, TwoDimension.NORTH, Distance.of(1,0));
        } else {
            return getGridCellFactory().moveOnce(startCell, TwoDimension.SOUTH, Distance.of(1,0));
        }
    }

    @Override
    public Collection<Square> attackPawnStyle(Square startCell, Player player) {
        validatePosition(startCell);
        final List<Square> list = new ArrayList<>();
        TwoDimension[] dirs = player == Player.WHITE
                ? new TwoDimension[] {TwoDimension.NORTHWEST, TwoDimension.NORTHEAST}
                : new TwoDimension[] {TwoDimension.SOUTHWEST, TwoDimension.SOUTHEAST};
        for (TwoDimension dir: dirs) {
            getGridCellFactory().moveOnce(startCell, dir, Distance.of(1,0)).ifPresent(list::add);
        }
        return list;
    }

    @Override
    public GridCellBuilder<Square, TwoDimension> getGridCellFactory() {
        return cellBuilder;
    }

    private void validatePosition(Square cell) {
        if (!cellBuilder.withinRange(cell.getFile().getCoordinate().getIndex(),
                cell.getRank().getCoordinate().getIndex())) {
            throw new IllegalArgumentException("Square " + cell + " is out of boundary of this board!");
        }
    }

    @Override
    public Collection<Square> getAllPositions() {
        return getGridCellFactory().getAllPositions();
    }
}
