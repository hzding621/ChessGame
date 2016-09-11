package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Piece that is able to initiate a move that involes more than one
 */
public interface SpecialMovePiece<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    Collection<Move<C>> specialMove(B board, Player player);
}
