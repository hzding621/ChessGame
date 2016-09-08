package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.game.PieceInformation;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import chessgame.rule.RequiresPieceInformation;
import com.google.common.collect.ImmutableList;
import utility.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that implements Pawn piece moving logic
 */
public final class Pawn<P extends PieceClass> extends AbstractPiece<P> {

    public Pawn(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class PawnRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B> implements RequiresPieceInformation<C, P>{

        private final PieceInformation<C, P> pieceInformation;

        public PawnRule(B gridViewer, PieceInformation<C, P> pieceInformation) {
            super(gridViewer);
            this.pieceInformation = pieceInformation;
        }

        private Collection<C> initialMove(C position, Player player) {
            if (pieceInformation.getPieceMoveCount(boardViewer.getPiece(position).get()) == 0) {
                return CollectionUtils.asArrayList(boardViewer.moveForwardNoOverlap(position, player)
                        .flatMap(oneMove -> boardViewer.moveForwardNoOverlap(oneMove, player)));
            }
            return Collections.emptyList();
        }

        @Override
        public Collection<C> attacking(C position, Player player) {
            return boardViewer.attackPawnStyle(position, player);
        }

        @Override
        public Collection<C> basicMoves(C position, Player player) {
            List<C> list = CollectionUtils.asArrayList(boardViewer.moveForwardNoOverlap(position, player));
            list.addAll(initialMove(position, player));
            list.addAll(attacking(position, player).stream()
                    .filter(c -> boardViewer.isEnemy(c, player))
                    .collect(Collectors.toList()));
            return list;
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