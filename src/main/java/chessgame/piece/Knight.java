package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Class that implements Knight piece moving logic
 */
public final class Knight<P extends PieceClass> extends AbstractPiece<P> {

    public Knight(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Knight{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class KnightRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B> {

        public KnightRule(B gridViewer) {
            super(gridViewer);
        }

        private Optional<C> knightStyle(B board, C startPosition, D direction, boolean closeWise) {
            Optional<C> middlePosition = board.moveSteps(startPosition, direction, 1);
            if (!middlePosition.isPresent()) return Optional.empty();

            direction = closeWise ? (D) direction.getClockwise() : (D) direction.getCounterClockwise();
            return board.moveSteps(middlePosition.get(), direction, 1);
        }

        @Override
        public Collection<C> attacking(C position, Player player) {
            final List<C> targets = new ArrayList<>();
            boardViewer.getOrthogonalDirections().forEach(direction -> {
                knightStyle(boardViewer, position, direction, true).ifPresent(targets::add);
                knightStyle(boardViewer, position, direction, false).ifPresent(targets::add);
            });
            return targets;
        }

        @Override
        public Collection<C> attackBlockingPositions(C sourcePosition, C targetPosition, Player player) {
            if (!attacking(sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return ImmutableList.of(sourcePosition, targetPosition);
        }
    }
}