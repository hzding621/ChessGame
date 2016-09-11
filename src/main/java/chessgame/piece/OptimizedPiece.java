package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Chess pieces implementing this rule can be optimized in move finding
 */
public interface OptimizedPiece<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    /**
     * Finds all positions the opponent can move to to block the attacking
     * including the position of the attacking piece itself
     *
     * @param board the given board
     * @param sourcePosition the position of the piece
     * @param targetPosition the position of the target piece the piece it is attacking
     * @param player the player the piece belongs to
     * @return all positions the opponent can move to to block the attacking
     */
    Collection<C> attackBlockingPositions(B board, C sourcePosition, C targetPosition, Player player);
}
