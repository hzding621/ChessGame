package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Vector;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This type of piece attack in symmetric directions, such as Rook, Bishop, Queen
 * Such pieces must be associated with GridViewer
 */
public interface Rider<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> extends LatentAttackPiece<C, P, B> {

    /**
     * @return the directions from which the piece can perform range attacks
     * @param board
     */
    Collection<D> getAttackingDirections(B board);

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        Collection<C> u = getAttackingDirections(board).stream()
                .flatMap(direction -> board.furthestReach(position, direction, Vector.of(1, 0), false, true).stream())
                .collect(Collectors.toList());
        return u;
    }

    @Override
    default Collection<LatentAttack<C>> latentAttacking(B board, C position, Player player) {
        return getAttackingDirections(board).stream()
                .map(direction -> board.firstAndSecondOccupant(position, direction, Vector.of(1,0))
                        .filter(pair -> board.isEnemy(pair.second(), player))
                        .map(pair -> new LatentAttack<>(position, pair.first(), pair.second(),
                                board.furthestReach(position, direction, Vector.of(1, 0), true, false))))
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
        return board.furthestReach(sourcePosition, direction, Vector.of(1, 0), true, false);
    }
}
