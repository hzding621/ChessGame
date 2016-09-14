package core.game;

import core.board.Tile;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;

/**
 * Piece Information contains runtime information such as what pieces have moved, where are kings, etc.
 */
public interface PieceInformation<C extends Tile, P extends PieceClass> {

    int getPieceMoveCount(Piece<P> piece);

    C locateKing(Player player);
}
