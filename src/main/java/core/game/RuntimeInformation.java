package core.game;

import core.board.Tile;
import core.piece.PieceClass;

/**
 * Stub class for different types of runtime information
 */
public interface RuntimeInformation<C extends Tile, P extends PieceClass> {

    PlayerInformation getPlayerInformation();

    PieceInformation<C, P> getPieceInformation();

    AttackInformation<C> getAttackInformation();
}
