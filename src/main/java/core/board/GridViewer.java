package core.board;

import core.piece.PieceClass;
import core.player.Player;
import utility.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A GridViewer is a BoardViewer that supports the notion of orthogonal and diagonal directions.
 */
public interface GridViewer<C extends Tile, D extends Direction<D>, P extends PieceClass>
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
     * Move the startTile at direction for the given number of steps
     * @param startTile starting tile
     * @param direction the direction to move
     * @param steps the given number
     * @param stepSize a step magnifier/modifier
     * @return non-empty value if the tile is not at the edge of the board, empty otherwise
     */
    Optional<C> travelSteps(C startTile, D direction, int steps, StepSize stepSize);

    /**
     * Returns a series of tile movement that starts at a tile, goes at a certain direction,
     * and either stops at first occupant or at the edge of board
     *  @param startTile the tile the movement starts at
     * @param direction the direction the movement goes at
     * @param stepSize a step magnifier/modifier
     * @param startInclusive whether or not to include the startTile
     * @param endInclusive whether or not to include the occupant if there exists  @return the list of movement
     */
    List<C> travelUntilBlocked(C startTile, D direction, StepSize stepSize, boolean startInclusive, boolean endInclusive);

    /**
     * @param startTile the given tile
     * @param direction the given direction
     * @param stepSize a step magnifier/modifier
     * @return non-empty if moving at the given tile in the given direction will lead to this piece, empty if it will lead the edge of the board
     */
    Optional<C> firstEncounter(C startTile, D direction, StepSize stepSize);

    /**
     * Return the first and second occupant met starting at the given tile and moving toward the given direction
     * @param startTile the given tile
     * @param direction the given direction
     * @param stepSize a step magnifier/modifier
     * @return Non-empty if there exists two such pieces
     */
    default Optional<Pair<C, C>> firstTwoEncounters(C startTile, D direction, StepSize stepSize) {
        Optional<C> firstMeet = firstEncounter(startTile, direction, stepSize);
        if (!firstMeet.isPresent()) return Optional.empty();
        Optional<C> secondMeet = firstEncounter(firstMeet.get(), direction, stepSize);
        if (!secondMeet.isPresent()) return Optional.empty();
        return Optional.of(Pair.of(firstMeet.get(), secondMeet.get()));
    }

    /**
     * return the position where the piece starts at startTile and move toward the enemy's side by one step
     * @return empty if the startTile is at an edge
     */
    Optional<C> travelForward(C startTile, Player player);

    /**
     * same as travelForward except cannot land on a tile that is occupied
     * @return empty if the startTile is at an edge or the getForward position is occupied
     */
    default Optional<C> travelForwardBlockable(C startTile, Player player) {
        return travelForward(startTile, player)
                .flatMap(c -> isOccupied(c) ? Optional.empty() : Optional.of(c));
    }

    /**
     * @return the positions where the startTile is attacking in the pawn-style
     */
    Collection<C> attackPawnStyle(C startTile, Player player);

    /**
     * @return the direction from startTile to endTile
     */
    D findDirection(C startTile, C endTile);

    /**
     * Return the tile factory of this grid. Can be used to construct tile for querying the grid.
     * @return The tile factory of this grid
     */
    GridTileBuilder<C, D> getGridTileBuilder();


    /**
     * @return all possible positions on the board
     */
    Collection<C> getAllPositions();
}
