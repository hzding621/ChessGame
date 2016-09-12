package chessgame.rule;

import chessgame.board.Tile;

import java.util.Collection;

/**
 * Represents an attack and the positions to block the attack by the defender
 */
public final class Attack<C extends Tile> implements Comparable<Attack<C>> {

    private final C attacker;
    private final Collection<C> blockingPositions;

    public Attack(C attacker, Collection<C> blockingPositions) {
        this.attacker = attacker;
        this.blockingPositions = blockingPositions;
    }

    public C getAttacker() {
        return attacker;
    }

    public Collection<C> getBlockingPositions() {
        return blockingPositions;
    }

    @Override
    public int compareTo(Attack<C> o) {
        if (o == this) return 0;
        return attacker.compareTo(o.attacker);
    }
}