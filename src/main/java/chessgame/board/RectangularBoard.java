package chessgame.board;

import chessgame.piece.Piece;
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
public abstract class RectangularBoard<P extends PieceClass>
        extends AbstractBoard<Square, P> implements GridViewer<Square, TwoDimension, P> {

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
    public Collection<TwoDimension> getEveryDirections() {
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
    public Optional<Square> travelSteps(Square startCell, TwoDimension direction, int steps, StepSize stepSize) {
        validatePosition(startCell);
        Optional<Square> cell = Optional.of(startCell);
        while (steps > 0 && cell.isPresent()) {
            cell =  getGridCellBuilder().moveOnce(cell.get(), direction, stepSize);
            steps--;
        }
        return cell;
    }

    @Override
    public List<Square> travelUntilBlocked(Square startCell,
                                           TwoDimension direction,
                                           StepSize stepSize,
                                           boolean startInclusive,
                                           boolean endInclusive) {
        validatePosition(startCell);
        List<Square> cellList = new ArrayList<>();
        if (startInclusive) {
            cellList.add(startCell);
        }
        Optional<Square> nextCell = travelSteps(startCell, direction, 1, stepSize);
        Optional<Piece<P>> piece = Optional.empty();
        while (nextCell.isPresent()) {
            piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant
                break;
            }
            cellList.add(nextCell.get());
            nextCell = travelSteps(nextCell.get(), direction, 1, stepSize);
        }
        if (endInclusive && piece.isPresent()) {
            cellList.add(nextCell.get());
        }
        return cellList;
    }

    public Optional<Square> firstEncounter(Square startCell, TwoDimension direction, StepSize stepSize) {
        validatePosition(startCell);
        return CollectionUtils.last(travelUntilBlocked(startCell, direction, stepSize, false, true)).filter(this::isOccupied);
    }

    @Override
    public Optional<Square> travelForward(Square startCell, Player player) {
        validatePosition(startCell);
        if (player == Player.WHITE) {
            return getGridCellBuilder().moveOnce(startCell, TwoDimension.NORTH, StepSize.of(1,0));
        } else {
            return getGridCellBuilder().moveOnce(startCell, TwoDimension.SOUTH, StepSize.of(1,0));
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
            getGridCellBuilder().moveOnce(startCell, dir, StepSize.of(1,0)).ifPresent(list::add);
        }
        return list;
    }

    @Override
    public GridCellBuilder<Square, TwoDimension> getGridCellBuilder() {
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
        return getGridCellBuilder().getAllPositions();
    }
}
