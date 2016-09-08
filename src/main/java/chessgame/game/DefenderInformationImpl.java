package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;
import chessgame.rule.Rules;
import com.google.common.collect.*;

import java.util.*;

/**
 * Default implementation of DefenderInformation
 */
public class DefenderInformationImpl<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements DefenderInformation<C, P, B> {

    private final Set<C> isAttacked = new TreeSet<>();
    private final Set<Attack<C>> checkers = new TreeSet<>();
    private final SetMultimap<C, LatentAttack<C>> latentCheckersByBlocker = MultimapBuilder.treeKeys().hashSetValues().build();

    @Override
    public boolean isAttacked(C cell) {
        return isAttacked.contains(cell);
    }

    @Override
    public Set<Attack<C>> getCheckers() {
        return checkers;
    }

    @Override
    public Set<LatentAttack<C>> getLatentCheckersByBlocker(C blocker) {
        return latentCheckersByBlocker.get(blocker);
    }

    /**
     * This method recompute the entire defender information, which includes what pieces are currently under attack
     * which pieces are king-defenders (cannot move unless the move invalidates the pinning), and opponent pieces that
     * are checking the current king. This method runs after every move is made
     */
    public void refresh(B board, Rules<C, P, B> rules, PlayerInformation playerInformation, C actorKingPosition) {
        isAttacked.clear();
        checkers.clear();
        latentCheckersByBlocker.clear();
        Player defender = playerInformation.getDefender();

        // Iterate through all the pieces of the current defenders
        board.getPiecesForPlayer(playerInformation.getDefender())
            .stream()
            .forEach(defenderPosition -> {
                // Get the positions a defending piece are attacking
                rules.attacking(board, defenderPosition, defender)
                    .stream()
                    .forEach(targetPosition -> {
                        // Mark the position as being under attacked
                        isAttacked.add(targetPosition);

                        // If any of the position is the current actor's king, mark the attackers as checker
                        if (actorKingPosition.equals(targetPosition)) {
                            checkers.add(new Attack<>(defenderPosition,
                                    rules.attackBlockingPositions(board, defenderPosition, targetPosition, defender)));
                        }
                    });

                // Get the positions a defending piece are latently attacking
                rules.latentAttacking(board, defenderPosition, playerInformation.getDefender())
                        .stream()
                        .forEach(latentAttack -> {
                            if (latentAttack.getAttacked().equals(actorKingPosition)) {
                                // If the hided piece is same as actor's king, register the protecting piece in the kingDefenders
                                latentCheckersByBlocker.put(latentAttack.getBlocker(), latentAttack);
                            }
                        });
            });
    }
}
