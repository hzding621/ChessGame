package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This type of piece attack in symmetric directions, such as Rook, Bishop, Queen
 * Such pieces must be associated with GridViewer
 */
public interface RangeAttackPieceRule<C extends Cell, D extends Direction, A extends PieceType, P extends Piece<A>,
        B extends GridViewer<C, D, A, P>> extends PinningPieceRule<C, A, P, B> {

    /**
     * @return the directions from which the piece can perform range attacks
     */
    Collection<D> getAttackingDirections();

    @Override
    default Collection<C> attacking(C position, Player player) {
        List<C> moveTos = new ArrayList<>();
        getAttackingDirections().forEach(direction ->
                getBoardViewer().furthestReach(position, direction).forEach(moveTos::add));
        return moveTos;
    }

    @Override
    default Collection<Pin<C>> pinningAttack(C position, Player player) {
        List<Pin<C>> pins = new ArrayList<>();
        getAttackingDirections().forEach(direction ->
                getBoardViewer().findPin(position, direction, player).ifPresent(pins::add));
        return pins;
    }

    @Override
    default Collection<C> getBlockingPositionsWhenAttacking(C sourcePosition,
                                                            C targetPosition,
                                                            Player player) {
        if (!isAttacking(sourcePosition, targetPosition, player)) {
            throw new IllegalArgumentException(sourcePosition + " is not attacking " + targetPosition + " !");
        }
        D direction = getBoardViewer().findDirection(sourcePosition, targetPosition);
        List<C> range = getBoardViewer().furthestReach(sourcePosition, direction);
        range.add(sourcePosition);
        return range;
    }
}
