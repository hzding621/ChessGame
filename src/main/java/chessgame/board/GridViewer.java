package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.Pin;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A GridViewer is a BoardViewer that supports the notion of orthogonal and diagonal directions.
 */
public interface GridViewer<C extends Cell, D extends Direction, A extends PieceType, P extends Piece<A>>
        extends BoardViewer<C, A, P> {

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
     * Returns a series of cell movement that starts at a cell, goes at a certain direction,
     * and either stops at first occupant or at the edge of board
     *
     * @param startCell the cell the movement starts at
     * @param direction the direction the movement goes at
     * @return the list of movement
     */
    List<C> furthestReach(C startCell, D direction);

    /**
     * Return if exists the pinned pieces a piece at startCell is pinning.
     * Both of the pinned pieces must belong to enemy
     * @param startCell the location of the player's piece
     * @param direction the direction of the
     * @param player the player
     * @return Non-empty if the pinning situation exists.
     */
    Optional<Pin<C>> findPin(C startCell, D direction, Player player);

    /**
     * return the position where the piece starts at startCell and move toward the enemy's side by one step
     * @return empty if the startCell is at an edge
     */
    Optional<C> moveForward(C startCell, Player player);

    /**
     * same as moveForward except cannot land on a cell that is occupied
     * @return empty if the startCell is at an edge or the forward position is occupied
     */
    Optional<C> moveForwardNoOverlap(C startCell, Player player);

    /**
     * @return the positions where the startCell is attacking in the pawn-style
     */
    Collection<C> attackPawnStyle(C startCell, Player player);

    /**
     * @return the direction from startCell to endCell
     */
    D findDirection(C startCell, C endCell);

    /**
     * Return the cell factory of this grid. Can be used to construct cell for querying the grid.
     * @return The cell factory of this grid
     */
    GridCellFactory<C, D> getGridCellFactory();
}
