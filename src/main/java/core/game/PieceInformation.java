package core.game;

import core.board.Tile;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;

/**
 * Piece Information contains runtime information such as what pieces have moved, where are kings, etc.
 */
public interface PieceInformation<C extends Tile, P extends PieceClass> {

    /**
     * @return the number of moves the given piece has made in the game
     */
    int getPieceMoveCount(Piece<P> piece);

    /**
     * @return the position of king of player
     */
    C locateKing(Player player);
}
