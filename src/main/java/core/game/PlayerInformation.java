package core.game;

import core.player.Player;

/**
 * Stores who is the actor and who is the defender of the current game
 */
public interface PlayerInformation {

    Player getActor();

    Player getDefender();

    /**
     * Go to next round, and switch actor and defender
     */
    void nextRound();
}
