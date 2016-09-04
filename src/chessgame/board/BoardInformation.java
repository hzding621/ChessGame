package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.Collection;

/**
 * Stores information about the board at runtime. Is NOT responsible for computing the information.
 */
public interface BoardInformation<C extends Cell, A extends PieceType, P extends Piece<A>> {
    /**
     * @return all cells that are attacking `attacked`
     */
    Collection<C> getAttackers(C attacked);
}
