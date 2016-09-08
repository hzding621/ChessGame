package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This type of piece attack in symmetric directions, such as Rook, Bishop, Queen
 * Such pieces must be associated with GridViewer
 */
public interface RangeAttackPieceRule<C extends Cell, P extends PieceClass, D extends Direction,
        B extends GridViewer<C, D, P>> extends LatentAttackPiece<C, P, B> {

    /**
     * @return the directions from which the piece can perform range attacks
     */
    Collection<D> getAttackingDirections();

    @Override
    default Collection<C> attacking(C position, Player player) {
        return getAttackingDirections().stream()
                .flatMap(direction -> getBoardViewer().furthestReach(position, direction, false, true).stream())
                .collect(Collectors.toList());
    }

    @Override
    default Collection<LatentAttack<C>> latentAttacking(C position, Player player) {
        return getAttackingDirections().stream()
                .map(direction -> getBoardViewer().firstAndSecondOccupant(position, direction)
                        .filter(pair -> getBoardViewer().isEnemy(pair.second(), player))
                        .map(pair -> new LatentAttack<>(position, pair.first(), pair.second(),
                                getBoardViewer().furthestReach(position, direction, true, false))))
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    default Collection<C> attackBlockingPositions(C sourcePosition,
                                                  C targetPosition,
                                                  Player player) {
        if (!isAttacking(sourcePosition, targetPosition, player)) {
            throw new IllegalArgumentException(sourcePosition + " is not attacking " + targetPosition + " !");
        }
        D direction = getBoardViewer().findDirection(sourcePosition, targetPosition);
        return getBoardViewer().furthestReach(sourcePosition, direction, true, false);
    }
}
