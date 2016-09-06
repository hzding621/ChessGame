package chessgame.rule;

/**
 * Interface for moving rule for a certain type of piece
 */

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Abstraction of moving rule for the piece class,
 * handles piece moving logic
 */
public interface PieceRule<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        extends RequiresBoardView<C, P, B> {

    /**
     * Find all position on the board the piece is attacking, including position occupied by my own piece
     * @param position the position of player's piece
     * @return all attacking positions
     */
    Collection<C> attacking(C position, Player player);

    /**
     * Check whether the given piece is current attacking the target, but does not consider check-escapes
     * @param position the position of player's piece
     * @param target the target position
     * @param player the player the piece belongs to
     */
    default boolean isAttacking(C position, C target, Player player) {
        return attacking(position, player).contains(target);
    }

    /**
     * Finds all normal moves the piece can make, including captures, but does not consider check-escapes.
     * @param position the position of the piece
     * @param player the player the piece belongs to
     * @return all possible moves
     */
    default Collection<C> basicMoves(C position, Player player) {
        return attacking(position, player)
                .stream()
                .filter(c -> !getBoardViewer().isOccupied(c) || getBoardViewer().isEnemy(c, player))
                .collect(Collectors.toList());
    }

    /**
     * Finds all positions the opponent can move to to block the attacking
     * including the position of the attacking piece itself
     * @param sourcePosition the position of the piece
     * @param targetPosition the position of the target piece the piece it is attacking
     * @param player the player the piece belongs to
     * @return all positions the opponent can move to to block the attacking
     */
    Collection<C> getBlockingPositionsWhenAttacking(C sourcePosition, C targetPosition, Player player);
}