package chessgame.board;

import chessgame.move.BoardTransition;
import chessgame.piece.PieceClass;

import java.util.function.Function;

/**
 * A board that can apply a transition function to itself and returns a view of the transitioned board
 */
public interface Previewable<C extends Tile, P extends PieceClass, V extends BoardViewer<C, P>,
        M extends Board<C, P>> extends BoardViewer<C, P> {

    <T> T preview(BoardTransition<C, P, M> transition, Function<V, T> callback);
}
