package core.game;

import core.board.Tile;

import java.util.Set;

/**
 * Contains information computed from opponent pieces
 */
public interface AttackInformation<C extends Tile> {

    /**
     * @return whether the given position is currently under attacked by the defending side
     */
    boolean isAttacked(C tile);

    /**
     * @return the checkers information
     */
    Set<C> getCheckers();
}