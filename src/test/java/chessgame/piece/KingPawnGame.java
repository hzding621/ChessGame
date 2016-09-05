package chessgame.piece;

/**
 * Represents a piece type that only supports pawns and king, used for testing purposes
 */
public enum KingPawnGame implements PieceType {
    PAWN, KING;

    @Override
    public boolean isKing() {
        return this == KING;
    };
}
