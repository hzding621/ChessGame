package chessgame.board;

import chessgame.move.BoardTransition;
import chessgame.piece.PieceClass;

/**
 * A board that can apply a transition function to itself and returns a view of the transitioned board
 */
public interface Previewer<C extends Cell, P extends PieceClass, V extends BoardViewer<C, P>,
        M extends MutableBoard<C, P, M>> extends BoardViewer<C, P> {

    V preview(BoardTransition<C, P, M> transition);
}
