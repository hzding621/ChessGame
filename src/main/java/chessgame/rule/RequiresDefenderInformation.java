package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.game.DefenderInformation;
import chessgame.piece.PieceClass;

/**
 * Classes that implement this interface depend on runtime defender information.
 * Some special rule such as Castling requires this information to create its moving policy
 */
public interface RequiresDefenderInformation<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    DefenderInformation<C, P, B> getDefenderInformation();
}
