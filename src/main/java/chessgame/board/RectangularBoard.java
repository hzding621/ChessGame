package chessgame.board;

import chessgame.piece.Piece;
import chessgame.game.GameSetting;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.Pin;
import utility.CollectionUtils;

import java.util.*;

/**
 * Represents an abstract rectangular board
 */
public abstract class RectangularBoard<P extends PieceType>
        extends AbstractBoard<Square, P> implements GridViewer<Square, TwoDimension, P> {

    protected RectangularBoard(GameSetting<Square, P> gameSetting) {
        super(gameSetting);
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
    public Optional<Square> moveOnce(Square startCell, TwoDimension direction) {
        return getGridCellFactory().moveOnce(startCell, direction);
    }

    @Override
    public List<Square> furthestReach(Square startCell, TwoDimension direction) {
        List<Square> cellList = new ArrayList<>();
        Optional<Square> nextCell = moveOnce(startCell, direction);
        while (nextCell.isPresent()) {
            cellList.add(nextCell.get());
            Optional<Piece<P>> piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant
                break;
            }
            nextCell = moveOnce(nextCell.get(), direction);
        }
        return cellList;
    }

    private Optional<Square> firstOccupant(Square startCell, TwoDimension direction) {
        return CollectionUtils.last(furthestReach(startCell, direction))
                .filter(c -> getPiece(c).isPresent());
    }

    @Override
    public Optional<Pin<Square>> findPin(Square startCell, TwoDimension direction, Player player) {

        Optional<Square> firstMeet = firstOccupant(startCell, direction);
        if (!firstMeet.isPresent() || !isEnemy(firstMeet.get(), player)) return Optional.empty();
        Optional<Square> secondMeet = firstOccupant(firstMeet.get(), direction);
        if (!secondMeet.isPresent() || !isEnemy(secondMeet.get(), player)) return Optional.empty();
        return Optional.of(new Pin<>(startCell, firstMeet.get(), secondMeet.get()));
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
    public Optional<Square> moveForwardNoOverlap(Square startCell, Player player) {
        return moveForward(startCell, player)
                .flatMap(c -> isOccupied(c) ? Optional.empty() : Optional.of(c));
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
}
