package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;

import java.util.Set;

/**
 * Contains information computed from opponent pieces
 */
public interface DefenderInformation<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    /**
     * @return whether the given position is currently under attacked by the defending side
     */
    boolean isAttacked(C cell);

    /**
     * @return the checkers information
     */
    Set<Attack<C>> getCheckers();

    /**
     * @return all pinning situation wherein the input piece is the protecting piece
     */
    Set<LatentAttack<C>> getLatentCheckersByBlocker(C blocker);
}