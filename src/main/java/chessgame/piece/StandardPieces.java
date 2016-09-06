package chessgame.piece;

/**
 * Represent a standard Chess piece set of 32 pieces
 */
public enum StandardPieces implements PieceClass {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    @Override
    public boolean isKing() {
        return this == KING;
    };
}
