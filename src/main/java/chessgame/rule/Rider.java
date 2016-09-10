package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Distance;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This type of piece attack takes unlimited number of moves in certain directions with certain step size,
 * e.g.  Rook, Bishop, Queen
 */
public interface Rider<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends LatentAttackPiece<C, P, B> {

    /**
     * @param board the given board
     * @return the directions from which the piece can perform range attacks
     */
    Collection<D> getDirections(B board);

    /**
     * @return the distance by which the rider attacks
     */
    default Distance getDistance() {
        return Distance.of(1, 0);
    }

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        return getDirections(board).stream()
                .flatMap(direction -> board.furthestReach(position, direction, getDistance(), false, true).stream())
                .collect(Collectors.toList());
    }

    @Override
    default Collection<LatentAttack<C>> latentAttacking(B board, C position, Player player) {
        return getDirections(board).stream()
                .map(direction -> board.firstAndSecondOccupant(position, direction, getDistance())
                        .filter(pair -> board.isEnemy(pair.second(), player))
                        .map(pair -> new LatentAttack<>(position, pair.first(), pair.second(),
                                board.furthestReach(position, direction, getDistance(), true, false))))
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
        return board.furthestReach(sourcePosition, direction, getDistance(), true, false);
    }
}
