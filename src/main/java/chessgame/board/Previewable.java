package chessgame.board;

import chessgame.move.BoardTransition;
import chessgame.piece.PieceClass;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A board that can apply a transition function to itself and returns a view of the transitioned board
 */
public interface Previewable<C extends Cell, P extends PieceClass, V extends BoardViewer<C, P>,
        M extends MutableBoard<C, P, M>> extends BoardViewer<C, P> {

    <T> T preview(BoardTransition<C, P, M> transition, Function<V, T> callback);
}
