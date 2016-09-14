package core.piece;

import core.board.BoardViewer;
import core.board.Tile;
import core.player.Player;
import core.rule.LatentAttack;

import java.util.Collection;

/**
 * This interface exposes additional rule associated with pieces capable of making pinning/discovered attacks
 */
public interface LatentAttackPiece<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>>
        extends OptimizedPiece<C, P, B> {

    /**
     * Find pieces that form a pinning attack, both the protecting and protected pieces should belong to opponent
     *
     * @param board the given board
     * @param position the position of player's piece
     * @param player the player
     * @return The set of pinning attack the piece is making at position
     */
    Collection<LatentAttack<C>> latentAttacking(B board, C position, Player player);
}
