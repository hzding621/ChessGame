package core.piece;

import core.board.Tile;
import core.board.Direction;
import core.board.GridViewer;
import core.board.StepSize;
import core.player.Player;
import core.rule.LatentAttack;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This type of piece attack takes unlimited number of moves in certain directions with certain step size,
 * e.g.  Rook, Bishop, Queen
 */
public interface Rider<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends LatentAttackPiece<C, P, B> {

    /**
     * @param board the given board
     * @return the directions from which the piece can perform range attacks
     */
    Collection<D> getDirections(B board);

    /**
     * @return the distance by which the rider attacks
     */
    default StepSize getStepSize() {
        return StepSize.of(1, 0);
    }

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        return getDirections(board).stream()
                .flatMap(direction -> board.travelUntilBlocked(position, direction, getStepSize(), false, true).stream())
                .collect(Collectors.toList());
    }

    @Override
    default Collection<LatentAttack<C>> latentAttacking(B board, C position, Player player) {
        return getDirections(board).stream()
                .map(direction -> board.firstTwoEncounters(position, direction, getStepSize())
                        .filter(pair -> board.isEnemy(pair.second(), player))
                        .map(pair -> new LatentAttack<>(position, pair.first(), pair.second(),
                                board.travelUntilBlocked(position, direction, getStepSize(), true, false))))
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    default Collection<C> attackBlockingPositions(B board, C sourcePosition,
                                                  C targetPosition,
                                                  Player player) {
        if (!isAttacking(board, sourcePosition, targetPosition, player)) {
            throw new IllegalArgumentException(sourcePosition + " is not attacking " + targetPosition + " !");
        }
        D direction = board.findDirection(sourcePosition, targetPosition);
        return board.travelUntilBlocked(sourcePosition, direction, getStepSize(), true, false);
    }
}
