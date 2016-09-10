package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.Rules;

import java.util.Set;
import java.util.TreeSet;

/**
 * Default implementation of AttackInformation
 */
public final class AttackInformationImpl<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements AttackInformation<C> {

    private final Set<C> isAttacked = new TreeSet<>();
    private final Set<C> checkers = new TreeSet<>();

    private final B board;
    private final RuntimeInformation<C, P> runtimeInformation;

    public AttackInformationImpl(B board, RuntimeInformation<C, P> runtimeInformation) {
        this.board = board;
        this.runtimeInformation = runtimeInformation;
    }

    @Override
    public boolean isAttacked(C cell) {
        return isAttacked.contains(cell);
    }

    @Override
    public Set<C> getCheckers() {
        return checkers;
    }

    /**
     * This method recompute the entire defender information, which includes what pieces are currently under attack
     * which pieces are king-defenders (cannot move unless the move invalidates the pinning), and opponent pieces that
     * are checking the current king. This method runs after every move is made
     */
    public void update(Rules<C, P, B> rules) {
        isAttacked.clear();
        checkers.clear();

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();

        // Iterate through all the pieces of the current defenders
        board.getPiecesForPlayer(defender).forEach(defenderPosition -> {
            // Get the positions a defending piece are attacking
            rules.attacking(board, defenderPosition, defender).forEach(targetPosition -> {
                // Mark the position as being under attacked
                isAttacked.add(targetPosition);

                // If any of the position is the current actor's king, mark the attackers as checker
                if (runtimeInformation.getPieceInformation().locateKing(actor).equals(targetPosition)) {
                    checkers.add(defenderPosition);
                }
            });
        });
    }
}
