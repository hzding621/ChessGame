package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * A value type that represents a pair of cell and piece
 */
public final class PieceLocator<C extends Cell, A extends PieceType, P extends Piece<A>> {

    private final C cell;
    private final P piece;

    public PieceLocator(C cell, P piece) {
        this.piece = piece;
        this.cell = cell;
    }

    public P getPiece() {
        return piece;
    }

    public C getCell() {
        return cell;
    }
}
