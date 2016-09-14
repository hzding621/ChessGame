package core.piece;

import core.board.BoardViewer;
import core.board.Tile;
import core.move.Move;
import core.player.Player;

import java.util.Collection;

/**
 * Piece that is able to initiate a move that involves more than one
 */
public interface SpecialMovePiece<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    Collection<Move<C, P>> specialMove(B board, Player player);
}
