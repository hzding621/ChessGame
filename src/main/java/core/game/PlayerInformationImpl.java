package core.game;

import core.player.Player;

/**
 * Default implementation of PlayerInformation
 */
public final class PlayerInformationImpl implements PlayerInformation {
    private Player actor;
    private Player defender;

    public PlayerInformationImpl(Player actor, Player defender) {
        this.actor = actor;
        this.defender = defender;
    }

    public Player getActor() {
        return actor;
    }

    public Player getDefender() {
        return defender;
    }

    public void nextRound() {
        this.actor = actor.next();
        this.defender = defender.next();
    }
}
