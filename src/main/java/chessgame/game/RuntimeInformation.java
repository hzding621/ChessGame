package chessgame.game;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Contains useful information during the game
 */
public interface RuntimeInformation<C extends Cell, P extends PieceClass> {

    PlayerInformation getPlayerInformation();

    PieceInformation<C, P> getPieceInformation();

    AttackInformation<C> getAttackInformation();
}
