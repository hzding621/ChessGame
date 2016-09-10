package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Vector;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;
import utility.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A (mxn) Leaper moves a fixed type of vector between its start and arrival
 * A leaper's move cannot be blocked!
 */
public interface Leaper<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends OptimizedPiece<C, P, B> {

    /**
     * @return the vector by which the leaper attacks
     */
    Vector getAttackDirection();

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        final List<C> targets = new ArrayList<>();
        board.getOrthogonalDirections().forEach(direction -> {
            Optional<C> intermediate = board.moveSteps(position, direction, getAttackDirection().getX());
            if (!intermediate.isPresent()) return;
            board.moveSteps(intermediate.get(), direction.nextCloseWise().nextCloseWise(), getAttackDirection()
                    .getY()).ifPresent(targets::add);
            board.moveSteps(intermediate.get(), direction.nextCounterCloseWise().nextCounterCloseWise(), getAttackDirection()
                    .getY()).ifPresent(targets::add);
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
