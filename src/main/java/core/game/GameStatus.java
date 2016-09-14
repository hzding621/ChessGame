package core.game;

/**
 * Represents the current status of the game
 */
public enum GameStatus {


    /**
     * The Game is still open
     */
    OPEN,

    /**
     * One side has checkmated the other side: the other side has no legal move and is under check
     */
    CHECKMATE,


    /**
     * The game has reached to stalemate: one side has no legal but is not under check either
     */
    STALEMATE,


    /**
     * This is a special game status in which the game starts with no king and keeps playing forever
     * It is used for non-standard chess games.
     */
    PERPETUAL
}
