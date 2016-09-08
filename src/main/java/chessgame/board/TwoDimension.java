package chessgame.board;

/**
 * Represents the direction on a two dimensional board, i.e. X direction and Y direction
 */
public enum TwoDimension implements Direction {

    NORTH(0,1), NORTHEAST(1,1), EAST(1,0), SOUTHEAST(1,-1),
    SOUTH(0,-1), SOUTHWEST(-1,-1), WEST(-1,0), NORTHWEST(-1,1);

    public final static TwoDimension[] VALUES = values();
    public final static TwoDimension[] DIAGONALS = {NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST};
    public final static TwoDimension[] ORTHOGONALS = {NORTH, EAST, SOUTH, WEST};

    private final int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    TwoDimension(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TwoDimension reverse() {
        return VALUES[(this.ordinal() + 4) % VALUES.length];
    }

    @Override
    public TwoDimension getClockwise() {
        return VALUES[(this.ordinal()+1) % VALUES.length];
    }

    @Override
    public TwoDimension getCounterClockwise() {
        return VALUES[(this.ordinal()+ VALUES.length-1) % VALUES.length];
    }
}