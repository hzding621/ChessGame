package chessgame.rule;

import chessgame.board.Cell;

/**
 * Represents a set of pieces that form a king-pinning relationship
 */
public final class PinnedSet<C extends Cell> {
    private final C attacker;
    private final C protector;
    private final C hided;

    public PinnedSet(C attacker, C protector, C hided) {
        this.attacker = attacker;
        this.protector = protector;
        this.hided = hided;
    }

    /**
     * @return The attacker. The attacker is the piece that is making the pinning attack
     */
    public C getAttacker() {
        return attacker;
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
    public C getHided() {
        return hided;
    }
}
