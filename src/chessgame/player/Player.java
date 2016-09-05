package chessgame.player;

import java.util.Arrays;
import java.util.List;

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
