package chessgame.rule;

import chessgame.board.BoardView;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;

/**
 * PinningPieceRule exposes additional rule associated with pieces capable of making pinning/discovered attacks
 */
public interface PinningPieceRule<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardView<C, A, P>>
        extends PieceRule<C, A, P, B> {

    /**
     * Find pieces that form a pinning attack, both the protecting and protected pieces should belong to opponent
     * @param board the board to analyze on
     * @param position the position of player's piece
     * @param player the player
     * @return The set of pinning attack the piece is making at position
     */
    Collection<PinnedSet<C>> pinningAttack(B board, C position, Player player);
}
