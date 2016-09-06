package chessgame.move;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

import java.util.Optional;

/**
 * Represents a move that checks opponent king
 */
public class CheckMove<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        extends DetailedMove<C, P, B> {

    private final C opponentKing;

    public CheckMove(B board, C source, C target, Piece<P> sourcePiece, Optional<Piece<P>> capturedPiece, C opponentKing) {
        super(board, source, target, sourcePiece, capturedPiece);
        this.opponentKing = opponentKing;
    }

    public C getOpponentKing() {
        return opponentKing;
    }
}
