package chessgame.piece;

import chessgame.board.Tile;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.StepSize;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A (mxn) Leaper moves a fixed type of vector between its start and arrival
 * A leaper's move cannot be blocked!
 */
public interface Leaper<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends OptimizedPiece<C, P, B> {

    /**
     * @return the distance by which the leaper attacks
     */
    StepSize getDistance();

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        final Set<C> targets = new HashSet<>();
        board.getOrthogonalDirections().forEach(direction -> {
            int x = getDistance().getForward(), y = getDistance().getRotate();
            board.travelSteps(position, direction, 1, StepSize.of(x, y)).ifPresent(targets::add);
            board.travelSteps(position, direction, 1, StepSize.of(x, -y)).ifPresent(targets::add);
            board.travelSteps(position, direction, 1, StepSize.of(-x, y)).ifPresent(targets::add);
            board.travelSteps(position, direction, 1, StepSize.of(-x, -y)).ifPresent(targets::add);
        });
        return targets;
    }

    @Override
    default Collection<C> attackBlockingPositions(B board, C sourcePosition, C targetPosition, Player player) {
        if (!attacking(board, sourcePosition, player).contains(targetPosition)) {
            throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
        }

        // A leaper cannot be blocked
        return ImmutableList.of(sourcePosition);
    }
}
