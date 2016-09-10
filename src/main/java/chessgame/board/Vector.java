package chessgame.board;

/**
 * Represents a modifier to the a direction
 * X represents a magnification of current direction
 * Y represents a rotation and a maginification of the current direction
 *
 * e.g. NORTH with (1,2) becomes go north 1 step, then go east 2 steps
 */
public final class Vector {

    private final int x;

    private final int y;

    private Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector of(int x, int y) {
        return new Vector(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
