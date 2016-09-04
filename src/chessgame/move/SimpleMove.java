package chessgame.move;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.Optional;

/**
 * Created by haozhending on 9/4/16.
 */
public class SimpleMove<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>>
        implements Move<C, A, P, B> {

    private final B board;
    private final C source;
    private final C target;
    private final P sourcePiece;
    private final Optional<P> capturedPiece;

    public SimpleMove(B board, C source, C target, P sourcePiece, Optional<P> capturedPiece) {
        this.board = board;
        this.source = source;
        this.target = target;
        this.sourcePiece = sourcePiece;
        this.capturedPiece = capturedPiece;
    }

    public B getBoard() {
        return board;
    }

    public C getSource() {
        return source;
    }

    public C getTarget() {
        return target;
    }

    public P getSourcePiece() {
        return sourcePiece;
    }

    public Optional<P> getCapturedPiece() {
        return capturedPiece;
    }
}
