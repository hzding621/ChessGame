package chessgame.game;

import chessgame.board.Tile;
import chessgame.piece.PieceClass;

/**
 * Stub class for different types of runtime information
 */
public interface RuntimeInformation<C extends Tile, P extends PieceClass> {

    PlayerInformation getPlayerInformation();

    PieceInformation<C, P> getPieceInformation();

    AttackInformation<C> getAttackInformation();
}
