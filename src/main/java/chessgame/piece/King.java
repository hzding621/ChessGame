package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.game.PieceInformation;
import chessgame.player.Player;
import chessgame.rule.RequiresPieceInformation;
import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Class that implements King piece moving logic. Specific rules regarding king checking is handled elsewhere
 */
public final class King<P extends PieceClass> extends AbstractPiece<P> {

    public King(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "King{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static final class KingRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B> implements
            RequiresPieceInformation<C, P> {

        private final PieceInformation<C, P> pieceInformation;

        public KingRule(B gridViewer, PieceInformation<C, P> pieceInformation) {
            super(gridViewer);
            this.pieceInformation = pieceInformation;
        }

        @Override
        public Collection<C> attacking(C position, Player player) {
            final List<C> targets = new ArrayList<>();
            boardViewer.getAllDirections().stream()
                    .forEach(direction -> boardViewer.moveOnce(position, direction).ifPresent(targets::add));
            return targets;
        }

        @Override
        public Collection<C> attackBlockingPositions(C sourcePosition,
                                                     C targetPosition,
                                                     Player player) {
            if (!attacking(sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return ImmutableList.of(sourcePosition, targetPosition);
        }

        @Override
        public PieceInformation<C, P> getPieceInformation() {
            return pieceInformation;
        }
    }
}