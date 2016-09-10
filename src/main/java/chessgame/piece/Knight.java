package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Vector;
import chessgame.rule.Leaper;
import chessgame.rule.OptimizedPiece;
import chessgame.rule.PieceRule;

/**
 * Class that implements Knight piece moving logic
 */

public final class Knight<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>>
        implements Leaper<C, P, D, B>, OptimizedPiece<C, P, B>, PieceRule<C,P,B> {

    @Override
    public Vector getAttackDirection() {
        return Vector.of(1, 2);
    }
}