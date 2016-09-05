package chessgame.piece;

/**
 * Class that implements Knight piece moving logic
 */

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;

import java.util.*;

/**
 * Class that implements Knight piece moving logic
 */
public final class Knight<C extends Cell, A extends PieceType> extends AbstractPiece<C, A> {

    public Knight(A pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Knight{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class KnightRule<C extends Cell, A extends PieceType, D extends Direction, P extends Piece<A>,
            B extends GridViewer<C, D, A, P>> extends AbstractPieceRule<C, A, P, B> {

        public KnightRule(B gridViewer) {
            super(gridViewer);
        }

        private Optional<C> knightStyle(B board, C startPosition, D direction, boolean closeWise) {
            Optional<C> middlePosition = board.moveOnce(startPosition, direction);
            if (!middlePosition.isPresent()) return Optional.empty();

            direction = closeWise ? (D) direction.getClockwise() : (D) direction.getCounterClockwise();
            return board.moveOnce(middlePosition.get(), direction);
        }

        @Override
        public Collection<C> attacking(C position, Player player) {
            final List<C> targets = new ArrayList<>();
            boardViewer.getOrthogonalDirections().stream()
                .forEach(direction ->{
                        knightStyle(boardViewer, position, direction, true).ifPresent(targets::add);
                        knightStyle(boardViewer, position, direction, false).ifPresent(targets::add);
                });
            return targets;
        }

        @Override
        public Collection<C> getBlockingPositionsWhenAttacking(C sourcePosition,
                                                               C targetPosition,
                                                               Player player) {
            if (!attacking(sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return Arrays.asList(sourcePosition, targetPosition);
        }
    }
}