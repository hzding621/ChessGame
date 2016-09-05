package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridView;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This type of piece attack in symmetric directions, such as Rook, Bishop, Queen
 * Such pieces must be associated with GridView
 */
public interface DirectionalAttackingPieceRule<C extends Cell, D extends Direction, A extends PieceType, P extends Piece<A>,
        B extends GridView<C, D, A, P>> extends PinningPieceRule<C, A, P, B> {

    Collection<D> getAttackingDirections(B board);

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        List<C> moveTos = new ArrayList<>();
        getAttackingDirections(board).forEach(direction ->
                board.furthestReach(position, direction).forEach(moveTos::add));
        return moveTos;
    }

    @Override
    default Collection<PinnedSet<C>> pinningAttack(B board, C position, Player player) {
        List<PinnedSet<C>> pinnedSets = new ArrayList<>();
        getAttackingDirections(board).forEach(direction ->
                board.findPinnedSet(position, direction, player).ifPresent(pinnedSets::add));
        return pinnedSets;
    }

    @Override
    default Collection<C> getBlockingPositionsWhenAttacking(B board,
                                                           C sourcePosition,
                                                           C targetPosition,
                                                           Player player) {
        if (!canNormallyMoveTo(board, sourcePosition, targetPosition, player)) {
            throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
        }
        D direction = board.findDirection(sourcePosition, targetPosition);
        return board.furthestReach(sourcePosition, direction);
    }
}
