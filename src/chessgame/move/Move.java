package chessgame.move;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * Created by haozhending on 9/4/16.
 */
public interface Move<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {
}
