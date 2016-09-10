package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Vector;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import chessgame.rule.Leaper;
import chessgame.rule.OptimizedPiece;
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

    public static final class KnightRule<C extends Cell, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements Leaper<C, P, D, B>, OptimizedPiece<C, P, B> {

        @Override
        public Vector getAttackDirection() {
            return Vector.of(1, 2);
        }
    }
}