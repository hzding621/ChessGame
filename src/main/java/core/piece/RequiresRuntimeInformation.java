package core.piece;

import core.board.Tile;
import core.game.RuntimeInformation;

/**
 * Classes that implement this interface depend on runtime defender information.
 * Some special rule such as Castling requires this information to create its moving policy
 */
public interface RequiresRuntimeInformation<C extends Tile, P extends PieceClass> {

    RuntimeInformation<C, P> getRuntimeInformation();
}
