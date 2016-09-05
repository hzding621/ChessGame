package chessgame.rule;

/**
 * Interface for moving rule for a certain type of piece
 */

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Abstraction of moving rule for the piece class,
 * handles piece moving logic by passing in board information.
 */
public interface PieceRule<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    /**
     * Find all position on the board the piece is attacking, including position occupied by my own piece
     * @param board the board to analyze on
     * @param position the position of player's piece
     * @return all attacking positions
     */
    Collection<C> attacking(B board, C position, Player player);

    /**
     * Check whether the given piece can move to or capture the target piece, but does not consider check-escapes
     * @param board the board to analyze on
     * @param position the position of player's piece
     * @param target the target position
     * @param player the player the piece belongs to
     */
    default boolean canNormallyMoveTo(B board, C position, C target, Player player) {
        return basicMoves(board, position, player).contains(target);
    }

    /**
     * Finds all normal moves the piece can make, including captures, but does not consider check-escapes.
     * @param board the board to analyze on
     * @param position the position of the piece
     * @param player the player the piece belongs to
     * @return all possible moves
     */
    default Collection<C> basicMoves(B board, C position, Player player) {
        return attacking(board, position, player)
                .stream()
                .filter(c -> !board.getPiece(c).isPresent() || !board.getPiece(c).get().getPlayer().equals(player))
                .collect(Collectors.toList());
    }

    /**
     * Finds all positions the opponent can move to to block the attacking
     * including the position of the attacking piece itself
     * @param board the board to analyze on
     * @param sourcePosition the position of the piece
     * @param targetPosition the position of the target piece the piece it is attacking
     * @param player the player the piece belongs to
     * @return all positions the opponent can move to to block the attacking
     */
    Collection<C> getBlockingPositionsWhenAttacking(B board, C sourcePosition, C targetPosition, Player player);
}