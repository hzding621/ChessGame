package chessgame.board;

import chessgame.move.BoardTransition;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

/**
 * Represents a board in a chess game, which can be quried as well as updated
 */
public interface MutableBoard<C extends Cell, P extends PieceClass, T extends MutableBoard<C, P, T>>
        extends BoardViewer<C, P> {

    /**
     * Remove the piece at this position
     * @return the previous piece at this position
     */
    Piece<P> clearPiece(C position);

    /**
     * Move the piece from source to target
     * Throws IllegalStateException if source is empty or target is not empty
     * @param source cell to move from
     * @param target cell to move to
     * @return The moved piece
     */
    Piece<P> movePiece(C source, C target);

    /**
     * Add a piece at the given position
     */
    void addPiece(C position, Piece<P> piece);

    /**
     * @param boardTransition the board transition function
     * @return apply the board transition and return the transition result
     */
    TransitionResult<C, P> apply(BoardTransition<C, P, T> boardTransition);
}
