package chessgame.game;

import chessgame.board.Board;
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
public final class DefenderInformation<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    private final Set<C> isAttacked = new HashSet<>();
    private final Collection<PieceLocator<C, A, P>> checkers = new HashSet<>();
    private final Map<C, Collection<PinnedSet<C>>> kingDefenders = new HashMap<>();

    public Set<C> getIsAttacked() {
        return isAttacked;
    }

    public boolean isAttacked(C cell) {
        return isAttacked.contains(cell);
    }

    public Collection<PieceLocator<C, A, P>> getCheckers() {
        return checkers;
    }

    public Map<C, Collection<PinnedSet<C>>> getKingDefenders() {
        return kingDefenders;
    }

    public void refresh(B board, Rules<C, A, P, B> rules, PlayerInformation boardInformation, C actorKing) {
        isAttacked.clear();
        checkers.clear();
        kingDefenders.clear();

        // Iterate through all the pieces of the current defenders
        board.getAllPiecesForPlayer(boardInformation.getDefender())
            .parallelStream()
            .forEach(defenderPiece -> {
                // Get the positions a defending piece are attacking
                rules.attacking(board, defenderPiece.getCell(), boardInformation.getDefender())
                    .parallelStream()
                    .forEach(targetCell -> {
                        // Mark the position as being under attacked
                        isAttacked.add(targetCell);

                        // If any of the position is the current actor's king, mark the attackers as checker
                        if (actorKing.equals(targetCell)) {
                            checkers.add(defenderPiece);
                        }
                    });

                // Get the positions a defending piece is pinning
                rules.pinning(board, defenderPiece.getCell(), boardInformation.getDefender())
                    .parallelStream()
                    .forEach(pinnedSet -> {
                        if (pinnedSet.getHided().equals(actorKing)) {
                            // If the hided piece is same as actor's king, register the protecting piece in the kingDefenders
                            kingDefenders
                                .getOrDefault(pinnedSet.getHided(), new HashSet<>()).add(pinnedSet);
                        }
                    });

            });
    }

    /**
     * @return all pinning situation wherein the input piece is the protecting piece
     */
    public Collection<PinnedSet<C>> isKingDefender(C actorCell) {
        return kingDefenders.getOrDefault(actorCell, Collections.EMPTY_LIST);
    }
}
