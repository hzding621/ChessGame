package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.move.MovePath;
import chessgame.player.Player;
import chessgame.rule.PieceRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Queen<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Queen(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Queen{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public final class QueenRule<D extends Direction, P extends Piece<A>>
            implements PieceRule<C, A, P, GridBoard<C, D, A, P>> {

        @Override
        public Collection<C> getNormalMoves(GridBoard<C, D, A, P> board, C position, Player player) {
            List<C> moveTos = new ArrayList<>();
            board.getAllDirections().forEach(direction ->
                    board.furthestReachWithCapture(direction, position, player).getPath().forEach(moveTos::add));
            return moveTos;
        }

        @Override
        public Collection<C> getBlockingPositionsWhenAttacking(GridBoard<C, D, A, P> board,
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