package core.piece;

import core.board.BoardViewer;
import core.board.Tile;
import core.player.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Abstraction of moving rule for the piece class,
 * handles piece moving logic
 */
public interface PieceRule<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>> {

    /**
     * Find all positions on the board the piece is attacking
     * Note: This method considers positions, not pieces, so it will include positions occupied by my own pieces as well
     *
     * @param board the given board
     * @param position the position of player's piece
     * @return all attacking positions
     */
    Collection<C> attacking(B board, C position, Player player);

    /**
     * Check whether the given piece is current attacking the target, but does not consider check-escapes
     * @param board the given board
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
     * @param board the given board
     * @param position the position of the piece
     * @param player the player the piece belongs to
     * @return all possible moves
     */
    default Collection<C> basicMoves(B board, C position, Player player) {
        return attacking(board, position, player)
                .stream()
                .filter(c -> board.isEmpty(c) || board.isEnemy(c, player))
                .collect(Collectors.toList());
    }
}