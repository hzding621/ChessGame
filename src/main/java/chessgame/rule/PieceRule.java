package chessgame.rule;

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
public interface PieceRule<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    /**
     * Find all position on the board the piece is attacking, including position occupied by my own piece
     *
     * @param board
     * @param position the position of player's piece
     * @return all attacking positions
     */
    Collection<C> attacking(B board, C position, Player player);

    /**
     * Check whether the given piece is current attacking the target, but does not consider check-escapes
     * @param board
     * @param position the position of player's piece
     * @param target the target position
     * @param player the player the piece belongs to
     */
    default boolean isAttacking(B board, C position, C target, Player player) {
        return attacking(board, position, player).contains(target);
    }

    /**
     * Finds all normal moves the piece can make, including captures, but does not consider check-escapes.
     * The default implementation filter all the attacking positions of piece with the condition that the occupant is an enemy
     * (This is not the case for some pieces, such as Pawn, whose attacking rule and moving rule are different)
     *
     * @param board
     * @param position the position of the piece
     * @param player the player the piece belongs to
     * @return all possible moves
     */
    default Collection<C> basicMoves(B board, C position, Player player) {
        Collection<C> u = attacking(board, position, player)
                .stream()
                .filter(c -> !board.isOccupied(c) || board.isEnemy(c, player))
                .collect(Collectors.toList());
        return u;
    }

    /**
     * Finds all positions the opponent can move to to block the attacking
     * including the position of the attacking piece itself
     *
     * @param board
     * @param sourcePosition the position of the piece
     * @param targetPosition the position of the target piece the piece it is attacking
     * @param player the player the piece belongs to
     * @return all positions the opponent can move to to block the attacking
     */
    Collection<C> attackBlockingPositions(B board, C sourcePosition, C targetPosition, Player player);
}