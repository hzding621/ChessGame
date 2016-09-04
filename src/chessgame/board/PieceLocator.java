package chessgame.board;

import chessgame.piece.Piece;

/**
 * Created by haozhending on 9/4/16.
 */
public final class PieceLocator<C extends Cell> {

    private final C cell;
    private final Piece piece;

    public PieceLocator(C cell, Piece piece) {
        this.piece = piece;
        this.cell = cell;
    }

    public Piece getPiece() {
        return piece;
    }

    public C getCell() {
        return cell;
    }
}
