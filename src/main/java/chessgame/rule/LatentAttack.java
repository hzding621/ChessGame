package chessgame.rule;

import chessgame.board.Tile;

import java.util.Collection;

/**
 * A latent attack is a particular condition in the game in which a piece A is not directly attacking a piece B,
 * as the attack is blocked by a piece C, unless C moves to a position other than defined in a set P
 */
public final class LatentAttack<C extends Tile> {
    private final C attacker;
    private final C blocker;
    private final C attacked;
    private final Collection<C> maintainingPositions;

    public LatentAttack(C attacker, C blocker, C attacked, Collection<C> maintainingPositions) {
        this.attacker = attacker;
        this.blocker = blocker;
        this.attacked = attacked;
        this.maintainingPositions = maintainingPositions;
    }

    /**
     * @return The attacker. The attacker is the piece that is making the pinning attack
     */
    public C getAttacker() {
        return attacker;
    }

    /**
     * @return The blocker. The blocker is the piece that cannot move, otherwise it will expose the King
     */
    public C getBlocker() {
        return blocker;
    }
    /**
     * @return The Attacked Piece position
     */
    public C getAttacked() {
        return attacked;
    }

    /**
     * @return all positions where the blocker can move to without exposing the attacked to the attacker
     */
    public Collection<C> getMaintainingPositions() {
        return maintainingPositions;
    }
}