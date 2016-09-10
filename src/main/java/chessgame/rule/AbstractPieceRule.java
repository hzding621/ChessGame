package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;

/**
 * Abstract piece rule that depends on a board view. Standard piece rule usually can inherit this class
 */
public abstract class AbstractPieceRule<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
    implements PieceRule<C, P, B> {
}
