package chessgame.move;

import chessgame.board.Cell;

/**
 * Represents a possible move current player can make
 */
public final class SimpleMove<C extends Cell> implements Move<C>, Comparable<SimpleMove<C>> {

    public final C source;
    public final C target;

    private SimpleMove(C source, C target) {
        this.source = source;
        this.target = target;
    }

    public static <C extends Cell> SimpleMove<C> of(C source, C target) {
        return new SimpleMove<>(source, target);
    }

    @Override
    public C getSource() {
        return null;
    }

    @Override
    public C getTarget() {
        return null;
    }

    @Override
    public int compareTo(SimpleMove<C> o) {
        if (o == this) return 0;
        int a = this.source.compareTo(o.source);
        if (a != 0) return a;
        return this.target.compareTo(o.target);
    }

    @Override
    public String toString() {
        return "[" + source + "->" + target + "]";
    }
}
