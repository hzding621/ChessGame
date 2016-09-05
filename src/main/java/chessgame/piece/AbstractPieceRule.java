package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.rule.PieceRule;

/**
 * Abstract piece rule that depends on a board view. Standard piece rule usually can inherit this class
 */
public abstract class AbstractPieceRule<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardViewer<C, A, P>>
    implements PieceRule<C, A, P, B> {

    protected final B boardViewer;

    public AbstractPieceRule(B boardViewer) {
       this.boardViewer = boardViewer;
    }

    public B getBoardViewer() {
        return boardViewer;
    }
}
