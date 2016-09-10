package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.player.Player;

import java.util.Collection;

/**
 * A cannon moves just like the queen, but attack an opponent only if there is exactly one piece in between the cannon and the enemy
 */

public final class Cannon<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> implements chessgame.rule.PieceRule<C,P,B> {

    @Override
    public Collection<C> attacking(B board, C position, Player player) {
        return null;
    }
}