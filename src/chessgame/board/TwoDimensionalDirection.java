package chessgame.board;

public enum TwoDimensionalDirection implements Direction {

    NORTH(0,1), NORTHEAST(1,1), EAST(1,0), SOUTHEAST(1,-1),
    SOUTH(0,-1), SOUTHWEST(-1,-1), WEST(-1,0), NORTHWEST(-1,1);

    public final static TwoDimensionalDirection[] VALUES = values();
    public final static TwoDimensionalDirection[] DIAGONALS = {NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST};
    public final static TwoDimensionalDirection[] ORTHOGONALS = {NORTH, EAST, SOUTH, WEST};

    private final int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    TwoDimensionalDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Direction getClockwise() {
        return VALUES[(this.ordinal()+1) % VALUES.length];
    }

    @Override
    public Direction getCounterClockwise() {
        return VALUES[(this.ordinal()+ VALUES.length-1) % VALUES.length];
    }
}