package chessgame.board;

import chessgame.move.MovePath;
import chessgame.piece.ChessPieceType;
import chessgame.piece.Piece;
import chessgame.piece.PieceSet;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents an abstract rectangular board
 */
public abstract class RectangularBoard<A extends PieceType, P extends Piece<A>>
        extends AbstractBoard<SquareCell, A, P> implements GridBoard<SquareCell, SquareDirection, A, P> {

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
    public Optional<SquareCell> moveOnce(SquareCell startCell, SquareDirection direction) {
        return getGridCellFactory().moveOnce(startCell, direction);
    }

    @Override
    public SquareDirection findDirection(SquareCell startCell, SquareCell endCell) {
        return SquareCell.findDirection(startCell, endCell);
    }

    @Override
    public MovePath<SquareCell> furthestReachWithCapture(SquareDirection direction,
                                             SquareCell startCell,
                                             Player player) {
        List<SquareCell> cellList = new ArrayList<>();
        Optional<SquareCell> nextCell = moveOnce(startCell, direction);
        while (nextCell.isPresent()) {
            cellList.add(nextCell.get());
            Optional<P> piece = getPiece(nextCell.get());
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
}
