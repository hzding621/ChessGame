package chessgame.move;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.Optional;

/**
 * Represents a move wherein a piece moves from A to B. Contains details about the pieces
 */
public class DetailedMove<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardViewer<C, A, P>>
        implements Move<C> {

    private final B board;
    private final C source;
    private final C target;
    private final P sourcePiece;
    private final Optional<P> capturedPiece;

    public DetailedMove(B board, C source, C target, P sourcePiece, Optional<P> capturedPiece) {
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
