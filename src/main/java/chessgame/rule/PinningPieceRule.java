package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;

/**
 * This interface exposes additional rule associated with pieces capable of making pinning/discovered attacks
 */
public interface PinningPieceRule<C extends Cell, P extends PieceType, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    /**
     * Find pieces that form a pinning attack, both the protecting and protected pieces should belong to opponent
     * @param position the position of player's piece
     * @param player the player
     * @return The set of pinning attack the piece is making at position
     */
    Collection<Pin<C>> pinningAttack(C position, Player player);
}
