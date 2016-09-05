package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.board.Path;
import chessgame.player.Player;
import chessgame.rule.PieceRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Class that implements Pawn piece moving logic
 */
public final class Pawn<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Pawn(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class PawnRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridBoard<C, D, A, P>> implements PieceRule<C, A, P, B> {

        @Override
        public Collection<C> attacking(B board, C position, Player player) {
            return board.attackPawnStyle(position, player);
        }

        @Override
        public Collection<C> basicMoves(B board, C position, Player player) {
            return board.moveForward(position, player)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
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