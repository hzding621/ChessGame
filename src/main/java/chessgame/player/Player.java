package chessgame.player;

/**
 * Simple players that include only BLACK and WHITE
 * TODO: create Player interface
 */
public enum Player {
    WHITE, BLACK;

    public Player next() {
        return this == WHITE ? BLACK : WHITE;
    }
}
