package chessgame.game;

import chessgame.player.Player;

/**
 * Stores who is the actor and who is the defender of the current game
 */
public interface PlayerInformation {

    Player getActor();

    Player getDefender();
}
