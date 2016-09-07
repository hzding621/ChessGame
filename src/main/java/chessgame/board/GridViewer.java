package chessgame.board;

import chessgame.piece.PieceClass;
import chessgame.player.Player;
import utility.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A GridViewer is a BoardViewer that supports the notion of orthogonal and diagonal directions.
 */
public interface GridViewer<C extends Cell, D extends Direction, P extends PieceClass>
        extends BoardViewer<C, P> {

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
     * @param startInclusive whether or not to include the startCell
     * @param meetInclusive whether or not to include the occupant if there exists  @return the list of movement
     */
    List<C> furthestReach(C startCell, D direction, boolean startInclusive, boolean meetInclusive);

    /**
     * Return the first and second occupant met starting at the given cell and moving toward the given direction
     * @param startCell the given cell
     * @param direction the given direction
     * @return Non-empty if there exists two such pieces
     */
    Optional<Pair<C, C>> firstAndSecondOccupant(C startCell, D direction);

    /**
     * return the position where the piece starts at startCell and move toward the enemy's side by one step
     * @return empty if the startCell is at an edge
     */
    Optional<C> moveForward(C startCell, Player player);

    /**
     * same as moveForward except cannot land on a cell that is occupied
     * @return empty if the startCell is at an edge or the forward position is occupied
     */
    default Optional<C> moveForwardNoOverlap(C startCell, Player player) {
        return moveForward(startCell, player)
                .flatMap(c -> isOccupied(c) ? Optional.empty() : Optional.of(c));
    }

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
