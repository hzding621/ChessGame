package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Distance;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A (mxn) Leaper moves a fixed type of vector between its start and arrival
 * A leaper's move cannot be blocked!
 */
public interface Leaper<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends OptimizedPiece<C, P, B> {

    /**
     * @return the distance by which the leaper attacks
     */
    Distance getProjection();

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        final Set<C> targets = new HashSet<>();
        board.getOrthogonalDirections().forEach(direction -> {
            int x = getProjection().getForward(), y = getProjection().getRotate();
            board.moveSteps(position, direction, 1, Distance.of(x, y)).ifPresent(targets::add);
            board.moveSteps(position, direction, 1, Distance.of(x, -y)).ifPresent(targets::add);
            board.moveSteps(position, direction, 1, Distance.of(-x, y)).ifPresent(targets::add);
            board.moveSteps(position, direction, 1, Distance.of(-x, -y)).ifPresent(targets::add);
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
