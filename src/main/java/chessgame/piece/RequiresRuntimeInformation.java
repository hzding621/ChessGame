package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.game.AttackInformation;
import chessgame.game.RuntimeInformation;
import chessgame.piece.PieceClass;

/**
 * Classes that implement this interface depend on runtime defender information.
 * Some special rule such as Castling requires this information to create its moving policy
 */
public interface RequiresRuntimeInformation<C extends Cell, P extends PieceClass> {

    RuntimeInformation<C, P> getRuntimeInformation();
}
