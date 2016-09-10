package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;

import java.util.Collection;

/**
 * A cannon moves just like the queen, but attack an opponent only if there is exactly one piece in between the cannon and the enemy
 */
public final class Cannon<P extends PieceClass> extends AbstractPiece<P> {

    public Cannon(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "Cannon{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }

    public static final class CannonRule<C extends Cell, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B> {

        @Override
        public Collection<C> attacking(B board, C position, Player player) {
            return null;
        }
    }
}