package chessgame.board;

/**
 * Represents a modifier to a direction
 * X represents a magnification of current direction
 * Y represents a rotation and a maginification of the current direction
 *
 * e.g. NORTH with (1,2) becomes go north 1 step, then go east 2 steps
 */
public final class Projection {

    private final int forward;
    private final int rotate;

    private Projection(int straight, int rotate) {
        this.forward = straight;
        this.rotate = rotate;
    }

    public static Projection of(int x, int y) {
        return new Projection(x, y);
    }

    public int getForward() {
        return forward;
    }

    public int getRotate() {
        return rotate;
    }
}
