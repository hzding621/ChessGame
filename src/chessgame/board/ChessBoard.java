package chessgame.board;

import chessgame.move.MovePath;
import chessgame.piece.Piece;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends AbstractBoard<TwoDimensionalCell>
        implements GridBoard<TwoDimensionalCell, TwoDimensionalDirection> {

    private final Coordinate.Factory coordinateFactory = new Coordinate.Factory(8);
    private final TwoDimensionalCell.Factory cellFactory =
            new TwoDimensionalCell.Factory(coordinateFactory, coordinateFactory);

    @Override
    public Collection<Direction> getAllDirections() {
        return Arrays.asList(TwoDimensionalDirection.VALUES);
    }

    @Override
    public Collection<TwoDimensionalDirection> getOrthogonalDirections() {
        return Arrays.asList(TwoDimensionalDirection.ORTHOGONALS);
    }

    @Override
    public Collection<TwoDimensionalDirection> getDiagonalDirections() {
        return Arrays.asList(TwoDimensionalDirection.DIAGONALS);
    }

    @Override
    public MovePath furthestReachWithCapture(TwoDimensionalDirection direction,
                                             TwoDimensionalCell startCell,
                                             Player player) {
        List<Cell> cellList = new ArrayList<>();
        Optional<TwoDimensionalCell> nextCell = moveOnce(startCell, direction);
        while (nextCell.isPresent()) {
            cellList.add(nextCell.get());
            Optional<Piece> piece = getPiece(nextCell.get());
            if (piece.isPresent()) {
                // nextCell has an occupant

                if (piece.get().getPlayer().equals(player)) {
                    // do not include pieces of same player
                    cellList.remove(cellList.size() - 1);
                }
                break;
            }
            nextCell = moveOnce(nextCell.get(), direction);
        }
        return MovePath.of(cellList);
    }

    @Override
    public Optional<TwoDimensionalCell> moveOnce(TwoDimensionalCell startCell, TwoDimensionalDirection direction) {
        return cellFactory.moveOnce(startCell, direction);
    }
}
