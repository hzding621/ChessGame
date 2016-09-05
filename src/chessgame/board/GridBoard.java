package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.PinnedSet;

import java.util.Collection;
import java.util.List;
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
     * Move the startCell at direction for one step
     * @param startCell starting cell
     * @param direction the direction to move
     * @return non-empty value if the cell is not at the edge of the board, empty otherwise
     */
    Optional<C> moveOnce(C startCell, D direction);

    /**
     * Returns a series of cell movement that
     * starts at a cell, goes at a certain direction, and either stops at first occupant or at the edge of board
     *
     * @param startCell the cell the movement starts at
     * @param direction the direction the movement goes at
     * @return the list of movement
     */
    List<C> furthestReach(C startCell, D direction);

    Optional<PinnedSet<C>> findPinnedSet(C startCell, D direction, Player player);

    Optional<C> moveForward(C startCell, Player player);

    Collection<C> attackPawnStyle(C startCell, Player player);

    /**
     * @return the direction from startCell to endCell
     */
    D findDirection(C startCell, C endCell);

    GridCellFactory<C, D> getGridCellFactory();
}
