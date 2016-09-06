package chessgame.move;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

import java.util.Optional;

/**
 * Represents a move wherein a piece moves from A to B. Contains details about the pieces
 */
public class DetailedMove<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements Move<C> {

    private final B board;
    private final C source;
    private final C target;
    private final Piece<P> sourcePiece;
    private final Optional<Piece<P>> capturedPiece;

    public DetailedMove(B board, C source, C target, Piece<P> sourcePiece, Optional<Piece<P>> capturedPiece) {
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

    public Piece<P> getSourcePiece() {
        return sourcePiece;
    }

    public Optional<Piece<P>> getCapturedPiece() {
        return capturedPiece;
    }
}
