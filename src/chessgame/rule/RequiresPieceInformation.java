package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.game.PieceInformation;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * Classes that implement this interface depend on runtime piece information.
 * PieceRule such as Pawn requires this information to create its moving policy
 */
public interface RequiresPieceInformation<C extends Cell, A extends PieceType, P extends Piece<A>> {

    PieceInformation<C, A, P> getPieceInformation();
}
