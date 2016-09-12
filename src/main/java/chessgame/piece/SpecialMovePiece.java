package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Tile;
import chessgame.move.Move;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Piece that is able to initiate a move that involes more than one
 */
public interface SpecialMovePiece<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    Collection<Move<C, P>> specialMove(B board, Player player);
}
