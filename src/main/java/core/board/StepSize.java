package core.board;

/**
 * Represents a modifier to a direction
 * X represents a direction of current direction
 * Y represents a rotation and the distance on that direction
 *
 * e.g. NORTH with (1,2) becomes go north 1 step, then go east 2 steps
 */
public final class StepSize {

    private final int forward;
    private final int rotate;

    private StepSize(int straight, int rotate) {
        this.forward = straight;
        this.rotate = rotate;
    }

    public static StepSize of(int x, int y) {
        return new StepSize(x, y);
    }

    public int getForward() {
        return forward;
    }

    public int getRotate() {
        return rotate;
    }
}
