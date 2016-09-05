package chessgame.board;

import chessgame.piece.Piece;
import chessgame.piece.PieceType;

/**
 * A value type that represents a pair of cell and piece.
 * Its purpose is to mitigate extra queries to BoardView to find Piece for a certain Cell
 * It should only live during one round of game for consistency
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
