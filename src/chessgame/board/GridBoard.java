package chessgame.board;

import chessgame.move.MovePath;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * A GridBoard is a Board that supports the notion of orthogonal and diagonal directions.
 */
public interface GridBoard<C extends Cell, D extends Direction, A extends PieceType, P extends Piece<A>>
        extends Board<C, A, P> {

    /**
     * @return all directions a piece can potentially move that are supported by this board implementation
     */
    Collection<D> getAllDirections();

    /**
     * @return all orthogonal directions supported by this board
     */
    Collection<D> getOrthogonalDirections();

    /**
     * @return all diagonal directions supported by this board
     */
    Collection<D> getDiagonalDirections();

    /**
     * Returns a MovePath that represents a series of cell movement that
     * starts at a cell, goes at a certain direction, and either stops at first occupant or at the edge of board
     *
     * @param direction the direction the movement goes at
     * @param startCell the cell the movement starts at
     * @param player
     * @return the list of movement
     */
    MovePath<C> furthestReachWithCapture(D direction, C startCell, Player player);

    /**
     * Move the startCell at direction for one step
     * @param startCell starting cell
     * @param direction the direction to move
     * @return non-empty value if the cell is not at the edge of the board, empty otherwise
     */
    Optional<C> moveOnce(C startCell, D direction);

    /**
     * @return the direction from startCell to endCell
     */
    D findDirection(C startCell, C endCell);

    GridCellFactory<C, D> getGridCellFactory();
}
