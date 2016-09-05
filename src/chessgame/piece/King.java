package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.player.Player;
import chessgame.rule.DirectionalAttackingPieceRule;
import chessgame.rule.PieceRule;

import java.util.*;

/**
 * Class that implements King piece moving logic. Specific rules regarding king checking is handled elsewhere
 */
public final class King<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public King(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "King{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class KingRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridBoard<C, D, A, P>> implements PieceRule<C, A, P, B> {

        @Override
        public Collection<C> attacking(B board, C position, Player player) {
            final List<C> targets = new ArrayList<>();
            board.getAllDirections().stream()
                    .forEach(direction -> board.moveOnce(position, direction).ifPresent(targets::add));
            return targets;
        }

        @Override
        public Collection<C> getBlockingPositionsWhenAttacking(B board,
                                                               C sourcePosition,
                                                               C targetPosition,
                                                               Player player) {
            if (!attacking(board, sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return Arrays.asList(sourcePosition, targetPosition);
        }
    }
}