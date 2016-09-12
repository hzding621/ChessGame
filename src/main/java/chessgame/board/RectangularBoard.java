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

    protected final Square.Builder tileBuilder;

    protected RectangularBoard(Map<Square, Piece<P>> occupants, Square.Builder tileBuilder) {
        super(occupants);
        this.tileBuilder = tileBuilder;
    }

    @Override
    public boolean isOccupied(Square tile) {
        validatePosition(tile);
        return super.isOccupied(tile);
    }

    @Override
    public boolean isEnemy(Square tile, Player player) {
        validatePosition(tile);
        return super.isEnemy(tile, player);
    }

    @Override
    public Optional<Piece<P>> getPiece(Square tile) {
        validatePosition(tile);
        return super.getPiece(tile);
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
    public TwoDimension findDirection(Square startTile, Square endTile) {
        validatePosition(startTile);
        validatePosition(endTile);
        return Square.findDirection(startTile, endTile);
    }

    @Override
    public Optional<Square> travelSteps(Square startTile, TwoDimension direction, int steps, StepSize stepSize) {
        validatePosition(startTile);
        Optional<Square> tile = Optional.of(startTile);
        while (steps > 0 && tile.isPresent()) {
            tile =  getGridTileBuilder().moveOnce(tile.get(), direction, stepSize);
            steps--;
        }
        return tile;
    }

    @Override
    public List<Square> travelUntilBlocked(Square startTile,
                                           TwoDimension direction,
                                           StepSize stepSize,
                                           boolean startInclusive,
                                           boolean endInclusive) {
        validatePosition(startTile);
        List<Square> tileList = new ArrayList<>();
        if (startInclusive) {
            tileList.add(startTile);
        }
        Optional<Square> nextTile = travelSteps(startTile, direction, 1, stepSize);
        Optional<Piece<P>> piece = Optional.empty();
        while (nextTile.isPresent()) {
            piece = getPiece(nextTile.get());
            if (piece.isPresent()) {
                // nextTile has an occupant
                break;
            }
            tileList.add(nextTile.get());
            nextTile = travelSteps(nextTile.get(), direction, 1, stepSize);
        }
        if (endInclusive && piece.isPresent()) {
            tileList.add(nextTile.get());
        }
        return tileList;
    }

    public Optional<Square> firstEncounter(Square startTile, TwoDimension direction, StepSize stepSize) {
        validatePosition(startTile);
        return CollectionUtils.last(travelUntilBlocked(startTile, direction, stepSize, false, true)).filter(this::isOccupied);
    }

    @Override
    public Optional<Square> travelForward(Square startTile, Player player) {
        validatePosition(startTile);
        if (player == Player.WHITE) {
            return getGridTileBuilder().moveOnce(startTile, TwoDimension.NORTH, StepSize.of(1,0));
        } else {
            return getGridTileBuilder().moveOnce(startTile, TwoDimension.SOUTH, StepSize.of(1,0));
        }
    }

    @Override
    public Collection<Square> attackPawnStyle(Square startTile, Player player) {
        validatePosition(startTile);
        final List<Square> list = new ArrayList<>();
        TwoDimension[] dirs = player == Player.WHITE
                ? new TwoDimension[] {TwoDimension.NORTHWEST, TwoDimension.NORTHEAST}
                : new TwoDimension[] {TwoDimension.SOUTHWEST, TwoDimension.SOUTHEAST};
        for (TwoDimension dir: dirs) {
            getGridTileBuilder().moveOnce(startTile, dir, StepSize.of(1,0)).ifPresent(list::add);
        }
        return list;
    }

    @Override
    public GridTileBuilder<Square, TwoDimension> getGridTileBuilder() {
        return tileBuilder;
    }

    private void validatePosition(Square tile) {
        if (!tileBuilder.withinRange(tile.getFile().getCoordinate().getIndex(),
                tile.getRank().getCoordinate().getIndex())) {
            throw new IllegalArgumentException("Square " + tile + " is out of boundary of this board!");
        }
    }

    @Override
    public Collection<Square> getAllPositions() {
        return getGridTileBuilder().getAllPositions();
    }
}
