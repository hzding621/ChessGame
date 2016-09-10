package chessgame.piece;

/**
 * A set that include all extension pieces
 */
public enum ExtensionPieces implements PieceClass {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, ASSASSIN, GHOST;

    @Override
    public boolean isKing() {
        return this == KING;
    }

    @Override
    public boolean canCapture() {
        return this != GHOST;
    }
}
