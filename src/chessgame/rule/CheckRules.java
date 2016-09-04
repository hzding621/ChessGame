package chessgame.rule;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for handling checking information
 */
public final class CheckRules<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    public boolean movesRemovesCheck(C movedToPosition, B board, Player player,
                                     BoardInformation<C, A, P> information,
                                     PieceRulesBindings<C, A, P, B> rulesBindings) {

        int remainingCheckers = information.getCheckers().size();
        for (PieceLocator<C, A, P> checker: information.getCheckers()) {
            if (rulesBindings.getRule(checker.getPiece().getPieceClass())
                    .getBlockingPositionsWhenAttacking(board, checker.getCell(),
                            information.getKingPosition(player), player)
                    .stream()
                    .anyMatch(c -> c.equals(movedToPosition))) {
                remainingCheckers--;
            }
        }
        return remainingCheckers == 0;
    }
}
