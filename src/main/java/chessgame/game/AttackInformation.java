package chessgame.game;

import chessgame.board.Cell;

import java.util.Set;

/**
 * Contains information computed from opponent pieces
 */
public interface AttackInformation<C extends Cell> {

    /**
     * @return whether the given position is currently under attacked by the defending side
     */
    boolean isAttacked(C cell);

    /**
     * @return the checkers information
     */
    Set<C> getCheckers();

//    /**
//     * @return all pinning situation wherein the input piece is the protecting piece
//     */
//    Set<LatentAttack<C>> getLatentCheckersByBlocker(C blocker);
}