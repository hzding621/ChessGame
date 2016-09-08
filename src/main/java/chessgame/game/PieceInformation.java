package chessgame.game;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Piece Information contains runtime information such as what pieces have moved, where are kings, etc.
 */
public interface PieceInformation<C extends Cell, P extends PieceClass> {

    int getPieceMoveCount(Piece<P> piece);

    C locateKing(Player player);
}
