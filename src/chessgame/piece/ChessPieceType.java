package chessgame.piece;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represent a standard Chess piece set of 32 pieces
 */
public enum ChessPieceType implements PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    @Override
    public Collection<PieceType> getAllTypes() {
        return Arrays.asList(ChessPieceType.values());
    }
}
