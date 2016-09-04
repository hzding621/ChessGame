package chessgame.rule;

import chessgame.board.Cell;

/**
 * Represents a set of pieces that form a king-pinning relationship
 */
public final class KingPinning<C extends Cell> {
    private final C pinner;
    private final C protector;
    private final C kingPosition;

    public KingPinning(C pinner, C protector, C kingPosition) {
        this.pinner = pinner;
        this.protector = protector;
        this.kingPosition = kingPosition;
    }

    /**
     * @return The pinner. The pinner is the piece that is making the pinning attack
     */
    public C getPinner() {
        return pinner;
    }

    /**
     * @return The protector. The protector is the piece that cannot move, otherwise it will expose the King
     */
    public C getProtector() {
        return protector;
    }

    /**
     * @return The King's Position
     */
    public C getKingPosition() {
        return kingPosition;
    }
}
