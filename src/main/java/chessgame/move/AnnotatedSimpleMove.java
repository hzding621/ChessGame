package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

/**
 * Simple Move with stubbing of which piece is moving
 */
public class AnnotatedSimpleMove<C extends Cell, P extends PieceClass> extends SimpleMove<C> {

    private final Piece<P> piece;

    private AnnotatedSimpleMove(C source, C target, Player player, Piece<P> piece) {
        super(source, target, player);
        this.piece = piece;
    }

    public static <C extends Cell, P extends PieceClass> AnnotatedSimpleMove<C, P> of(Piece<P> piece, C source, C target, Player player) {
        return new AnnotatedSimpleMove<C, P>(source, target, player, piece);
    }

    @Override
    public String toString() {
        return piece.toString() + "[" + getInitiator() + "->" + getTarget() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SimpleMove)) return false;

        SimpleMove<?> that = (SimpleMove<?>) o;

        return that.equals(this);
    }
}
