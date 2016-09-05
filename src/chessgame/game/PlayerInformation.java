package chessgame.game;

import chessgame.player.Player;

/**
 * Stores who is the actor and who is the defender of the current game
 */
public final class PlayerInformation {
    private Player actor;
    private Player defender;

    public PlayerInformation(Player actor, Player defender) {
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
