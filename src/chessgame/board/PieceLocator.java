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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceLocator<?, ?, ?> that = (PieceLocator<?, ?, ?>) o;

        if (!cell.equals(that.cell)) return false;
        return piece.equals(that.piece);
    }

    @Override
    public int hashCode() {
        int result = cell.hashCode();
        result = 31 * result + piece.hashCode();
        return result;
    }
}
