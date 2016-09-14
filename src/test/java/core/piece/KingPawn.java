package core.piece;

/**
 * Represents a piece type that only supports pawns and king, used for testing purposes
 */
public enum KingPawn implements PieceClass {
    PAWN, KING;

    @Override
    public boolean isKing() {
        return this == KING;
    }

    @Override
    public boolean canCapture() {
        return true;
    }
}
