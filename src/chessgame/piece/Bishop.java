package chessgame.piece;

import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.board.Cell;
import chessgame.move.MovePath;
import chessgame.player.Player;
import chessgame.rule.PieceRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that implements Rook piece moving logic
 */
public final class Bishop<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Bishop(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Bishop{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class BishopRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridBoard<C, D, A, P>> implements PieceRule<C, A, P, B> {

        @Override
        public Collection<C> getNormalMoves(B board, C position, Player player) {
            List<C> moveTos = new ArrayList<>();
            board.getDiagonalDirections().forEach(direction ->
                    board.furthestReachWithCapture(direction, position, player).getPath().forEach(moveTos::add));
            return moveTos;
        }

        @Override
        public Collection<C> getBlockingPositionsWhenAttacking(B board,
                                                               C sourcePosition,
                                                               C targetPosition,
                                                               Player player) {
            if (!canNormallyMoveTo(board, sourcePosition, targetPosition, player)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }
            D direction = board.findDirection(sourcePosition, targetPosition);
            MovePath<C> movePath = board.furthestReachWithCapture(direction, sourcePosition, player);
            return movePath.getPath();
        }
    }
}