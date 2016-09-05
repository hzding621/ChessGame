package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.rule.PinnedSet;
import chessgame.rule.Rules;

import java.util.*;

/**
 * Contains information computed from opponent pieces
 */
public final class DefenderInformation<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardViewer<C, A, P>> {

    private final Set<C> isAttacked = new TreeSet<>();
    private final Collection<PieceLocator<C, A, P>> checkers = new TreeSet<>();
    private final Map<C, Collection<PinnedSet<C>>> kingDefenders = new TreeMap<>();

    /**
     * @return the set of positions that are currently under attacked by the defending side
     */
    public Set<C> getIsAttacked() {
        return isAttacked;
    }

    /**
     * @return whether the given position is currently under attacked by the defending side
     */
    public boolean isAttacked(C cell) {
        return isAttacked.contains(cell);
    }

    /**
     * @return the set of defending side pieces that are currently checking actor side king
     */
    public Collection<PieceLocator<C, A, P>> getCheckers() {
        return checkers;
    }

    /**
     * @return mapping from actor side pieces to pinning situation
     */
    public Map<C, Collection<PinnedSet<C>>> getKingDefenders() {
        return kingDefenders;
    }

    /**
     * This method recompute the entire defender information, which includes what pieces are currently under attack
     * which pieces are king-defenders (cannot move unless the move invalidates the pinning), and opponent pieces that
     * are checking the current king. This method runs after every move is made
     */
    public void refresh(B board, Rules<C, A, P, B> rules, PlayerInformation boardInformation, C actorKing) {
        isAttacked.clear();
        checkers.clear();
        kingDefenders.clear();

        // Iterate through all the pieces of the current defenders
        board.getAllPiecesForPlayer(boardInformation.getDefender())
            .stream()
            .forEach(defenderLocator -> {
                // Get the positions a defending piece are attacking
                rules.attacking(board, defenderLocator.getCell(), boardInformation.getDefender())
                    .stream()
                    .forEach(targetCell -> {
                        // Mark the position as being under attacked
                        isAttacked.add(targetCell);

                        // If any of the position is the current actor's king, mark the attackers as checker
                        if (actorKing.equals(targetCell)) {
                            checkers.add(defenderLocator);
                        }
                    });

                // Get the positions a defending piece is pinning
                rules.pinning(board, defenderLocator.getCell(), boardInformation.getDefender())
                    .stream()
                    .forEach(pinnedSet -> {
                        if (pinnedSet.getHided().equals(actorKing)) {
                            // If the hided piece is same as actor's king, register the protecting piece in the kingDefenders
                            kingDefenders.getOrDefault(pinnedSet.getHided(), new HashSet<>()).add(pinnedSet);
                        }
                    });

            });
    }

    /**
     * @return all pinning situation wherein the input piece is the protecting piece
     */
    public Collection<PinnedSet<C>> isKingDefender(C actorCell) {
        return kingDefenders.getOrDefault(actorCell, Collections.emptyList());
    }
}
