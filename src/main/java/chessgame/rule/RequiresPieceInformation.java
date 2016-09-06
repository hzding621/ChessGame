package chessgame.rule;

import chessgame.board.Cell;
import chessgame.game.PieceInformation;
import chessgame.piece.PieceType;

/**
 * Classes that implement this interface depend on runtime piece information.
 * PieceRule such as Pawn requires this information to create its moving policy
 */
public interface RequiresPieceInformation<C extends Cell, P extends PieceType> {

    PieceInformation<C, P> getPieceInformation();
}
