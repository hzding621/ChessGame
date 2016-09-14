package core.move;

import core.board.Tile;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;

/**
 * Simple Move with stubbing of which piece is moving
 */
public final class AnnotatedSimpleMove<C extends Tile, P extends PieceClass> extends SimpleMove<C, P> {

    private final Piece<P> piece;

    private AnnotatedSimpleMove(C source, C target, Player player, Piece<P> piece) {
        super(source, target, player);
        this.piece = piece;
    }

    public static <C extends Tile, P extends PieceClass> AnnotatedSimpleMove<C, P> of(Piece<P> piece, C source, C target, Player player) {
        return new AnnotatedSimpleMove<>(source, target, player, piece);
    }

    @Override
    public String toString() {
        return piece.toString() + "[" + getInitiator() + "->" + getTarget() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SimpleMove)) return false;

        SimpleMove<?, ?> that = (SimpleMove<?, ?>) o;

        return that.equals(this);
    }
}
