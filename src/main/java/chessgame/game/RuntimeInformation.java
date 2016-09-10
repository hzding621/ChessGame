package chessgame.game;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Stub class for different types of runtime information
 */
public interface RuntimeInformation<C extends Cell, P extends PieceClass> {

    PlayerInformation getPlayerInformation();

    PieceInformation<C, P> getPieceInformation();

    AttackInformation<C> getAttackInformation();
}
