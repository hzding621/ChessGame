package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceSet;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.PinnedSet;
import utility.CollectionUtils;

import java.util.*;

/**
 * Represents an abstract rectangular board
 */
public abstract class RectangularBoard<A extends PieceType, P extends Piece<A>>
        extends AbstractBoard<SquareCell, A, P> implements GridView<SquareCell, SquareDirection, A, P> {

    protected RectangularBoard(PieceSet<SquareCell, A, P> pieceSet) {
        super(pieceSet);
    }

    @Override
    public Collection<SquareDirection> getAllDirections() {
        return Arrays.asList(SquareDirection.VALUES);
    }

    @Override
    public Collection<SquareDirection> getOrthogonalDirections() {
        return Arrays.asList(SquareDirection.ORTHOGONALS);
    }

    @Override
    public Collection<SquareDirection> getDiagonalDirections() {
        return Arrays.asList(SquareDirection.DIAGONALS);
    }


    @Override
    public SquareDirection findDirection(SquareCell startCell, SquareCell endCell) {
        return SquareCell.findDirection(startCell, endCell);
    }

    @Override
    public Optional<SquareCell> moveOnce(SquareCell startCell, SquareDirection direction) {
        return getGridCellFactory().moveOnce(startCell, direction);
    }

    @Override
    public List<SquareCell> furthestReach(SquareCell startCell, SquareDirection direction) {
        List<SquareCell> cellList = new ArrayList<>();
        Optional<SquareCell> nextCell = moveOnce(startCell, direction);
        while (nextCell.isPresent()) {
            cellList.add(nextCell.get());
            Optional<P> piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant
                break;
            }
            nextCell = moveOnce(nextCell.get(), direction);
        }
        return cellList;
    }

    private Optional<SquareCell> firstOccupant(SquareCell startCell, SquareDirection direction) {
        return CollectionUtils.last(furthestReach(startCell, direction))
                .filter(c -> getPiece(c).isPresent());
    }

    @Override
    public Optional<PinnedSet<SquareCell>> findPinnedSet(SquareCell startCell, SquareDirection direction, Player player) {

        Optional<SquareCell> firstMeet = firstOccupant(startCell, direction);
        if (!firstMeet.isPresent() || !isEnemy(firstMeet.get(), player)) return Optional.empty();
        Optional<SquareCell> secondMeet = firstOccupant(firstMeet.get(), direction);
        if (!secondMeet.isPresent() || !isEnemy(secondMeet.get(), player)) return Optional.empty();
        return Optional.of(new PinnedSet<>(startCell, firstMeet.get(), secondMeet.get()));
    }

    @Override
    public Optional<SquareCell> moveForward(SquareCell startCell, Player player) {
        if (player == Player.WHITE) {
            return getGridCellFactory().moveOnce(startCell, SquareDirection.NORTH);
        } else {
            return getGridCellFactory().moveOnce(startCell, SquareDirection.SOUTH);
        }
    }

    @Override
    public Collection<SquareCell> attackPawnStyle(SquareCell startCell, Player player) {
        final List<SquareCell> list = new ArrayList<>();
        SquareDirection[] dirs = player == Player.WHITE
                ? new SquareDirection[] {SquareDirection.NORTHWEST, SquareDirection.NORTHEAST}
                : new SquareDirection[] {SquareDirection.SOUTHWEST, SquareDirection.SOUTHEAST};
        for (SquareDirection dir: dirs) {
            getGridCellFactory().moveOnce(startCell, dir).ifPresent(cell -> {
                if (isOccupied(cell)) {
                    list.add(cell);
                }
            });
        }
        return list;
    }
}
