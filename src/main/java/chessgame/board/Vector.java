package chessgame.board;

/**
 * Represents a 2D vector on a grid board
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
