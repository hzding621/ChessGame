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
public interface GridViewer<C extends Cell, D extends Direction<D>, P extends PieceClass>
        extends BoardViewer<C, P> {

    /**
     * @return all directions a piece can potentially move that are supported by this board implementation
     */
    Collection<D> getEveryDirections();

    /**
     * @return all orthogonal directions supported by this board
     */
    Collection<D> getOrthogonalDirections();

    /**
     * @return all diagonal directions supported by this board
     */
    Collection<D> getDiagonalDirections();

    /**
     * Move the startCell at direction for the given number of steps
     * @param startCell starting cell
     * @param direction the direction to move
     * @param steps the given number
     * @param stepSize a step magnifier/modifier
     * @return non-empty value if the cell is not at the edge of the board, empty otherwise
     */
    Optional<C> travelSteps(C startCell, D direction, int steps, StepSize stepSize);

    /**
     * Returns a series of cell movement that starts at a cell, goes at a certain direction,
     * and either stops at first occupant or at the edge of board
     *  @param startCell the cell the movement starts at
     * @param direction the direction the movement goes at
     * @param stepSize a step magnifier/modifier
     * @param startInclusive whether or not to include the startCell
     * @param endInclusive whether or not to include the occupant if there exists  @return the list of movement
     */
    List<C> travelUntilBlocked(C startCell, D direction, StepSize stepSize, boolean startInclusive, boolean endInclusive);

    /**
     * @param startCell the given cell
     * @param direction the given direction
     * @param stepSize a step magnifier/modifier
     * @return non-empty if moving at the given cell in the given direction will lead to this piece, empty if it will lead the edge of the board
     */
    Optional<C> firstEncounter(C startCell, D direction, StepSize stepSize);

    /**
     * Return the first and second occupant met starting at the given cell and moving toward the given direction
     * @param startCell the given cell
     * @param direction the given direction
     * @param stepSize a step magnifier/modifier
     * @return Non-empty if there exists two such pieces
     */
    default Optional<Pair<C, C>> firstTwoEncounters(C startCell, D direction, StepSize stepSize) {
        Optional<C> firstMeet = firstEncounter(startCell, direction, stepSize);
        if (!firstMeet.isPresent()) return Optional.empty();
        Optional<C> secondMeet = firstEncounter(firstMeet.get(), direction, stepSize);
        if (!secondMeet.isPresent()) return Optional.empty();
        return Optional.of(Pair.of(firstMeet.get(), secondMeet.get()));
    }

    /**
     * return the position where the piece starts at startCell and move toward the enemy's side by one step
     * @return empty if the startCell is at an edge
     */
    Optional<C> travelForward(C startCell, Player player);

    /**
     * same as travelForward except cannot land on a cell that is occupied
     * @return empty if the startCell is at an edge or the getForward position is occupied
     */
    default Optional<C> travelForwardBlockable(C startCell, Player player) {
        return travelForward(startCell, player)
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
    GridCellBuilder<C, D> getGridCellBuilder();


    /**
     * @return all possible positions on the board
     */
    Collection<C> getAllPositions();
}
