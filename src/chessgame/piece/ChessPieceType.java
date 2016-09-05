package chessgame.piece;

/**
 * Represent a standard Chess piece set of 32 pieces
 */
public enum ChessPieceType implements PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    @Override
    public boolean isKing() {
        return this == KING;
    };
}
