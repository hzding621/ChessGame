package chessgame.move;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.Optional;

/**
 * Represents a move that checks opponent king
 */
public class CheckMove<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>>
        extends SimpleMove<C, A, P, B>{

    private final C opponentKing;

    public CheckMove(B board, C source, C target, P sourcePiece, Optional<P> capturedPiece, C opponentKing) {
        super(board, source, target, sourcePiece, capturedPiece);
        this.opponentKing = opponentKing;
    }

    public C getOpponentKing() {
        return opponentKing;
    }
}
