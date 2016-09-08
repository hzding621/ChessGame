package chessgame.board;

import chessgame.piece.Piece;
import chessgame.game.GameSetting;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import utility.CollectionUtils;

import java.util.*;

/**
 * Represents an abstract rectangular board
 */
public class RectangularBoard<P extends PieceClass>
        extends AbstractBoard<Square, P> implements GridViewer<Square, TwoDimension, P> {

    private final Square.Builder cellBuilder;

    public RectangularBoard(GameSetting.GridGame<Square, P> gameSetting) {
        super(gameSetting);
        final Coordinate.Builder fileBuilder = new Coordinate.Builder(gameSetting.getFileLength());
        final Coordinate.Builder rankBuilder = new Coordinate.Builder(gameSetting.getRankLength());
        this.cellBuilder = new Square.Builder(fileBuilder, rankBuilder);
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
        return Square.findDirection(startCell, endCell);
    }

    @Override
    public Optional<Square> moveSteps(Square startCell, TwoDimension direction, int steps) {
        Optional<Square> cell = Optional.empty();
        while (steps > 0 && cell.isPresent()) {
             cell =  getGridCellFactory().moveOnce(startCell, direction);
            steps--;
        }
        return cell;
    }

    @Override
    public List<Square> furthestReach(Square startCell, TwoDimension direction, boolean startInclusive, boolean meetInclusive) {
        List<Square> cellList = new ArrayList<>();
        if (startInclusive) {
            cellList.add(startCell);
        }
        Optional<Square> nextCell = moveSteps(startCell, direction, 1);
        Optional<Piece<P>> piece = Optional.empty();
        while (nextCell.isPresent()) {
            piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant
                break;
            }
            cellList.add(nextCell.get());
            nextCell = moveSteps(nextCell.get(), direction, 1);
        }
        if (meetInclusive && piece.isPresent()) {
            cellList.add(nextCell.get());
        }
        return cellList;
    }

    public Optional<Square> firstOccupant(Square startCell, TwoDimension direction) {
        return CollectionUtils.last(furthestReach(startCell, direction, false, true)).filter(this::isOccupied);
    }

    @Override
    public Optional<Square> moveForward(Square startCell, Player player) {
        if (player == Player.WHITE) {
            return getGridCellFactory().moveOnce(startCell, TwoDimension.NORTH);
        } else {
            return getGridCellFactory().moveOnce(startCell, TwoDimension.SOUTH);
        }
    }

    @Override
    public Collection<Square> attackPawnStyle(Square startCell, Player player) {
        final List<Square> list = new ArrayList<>();
        TwoDimension[] dirs = player == Player.WHITE
                ? new TwoDimension[] {TwoDimension.NORTHWEST, TwoDimension.NORTHEAST}
                : new TwoDimension[] {TwoDimension.SOUTHWEST, TwoDimension.SOUTHEAST};
        for (TwoDimension dir: dirs) {
            getGridCellFactory().moveOnce(startCell, dir).ifPresent(list::add);
        }
        return list;
    }

    @Override
    public GridCellFactory<Square, TwoDimension> getGridCellFactory() {
        return cellBuilder;
    }
}
