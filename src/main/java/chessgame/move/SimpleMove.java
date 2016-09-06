package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a possible move current player can make
 */
public final class SimpleMove<C extends Cell> implements Move<C>, Comparable<SimpleMove<C>> {

    public final C source;
    public final C target;
    public final Player player;

    private SimpleMove(C source, C target, Player player) {
        this.source = source;
        this.target = target;
        this.player = player;
    }

    public static <C extends Cell> SimpleMove<C> of(C source, C target, Player player) {
        return new SimpleMove<>(source, target, player);
    }

    @Override
    public C getSource() {
        return source;
    }

    @Override
    public C getTarget() {
        return target;
    }

    @Override
    public <P extends PieceClass> BoardTransition<C, P> getTransition() {
        return board -> {
            if (!board.getPiece(source).isPresent()) {
                throw new IllegalStateException("Invalid move. Source cell " + source + " is empty!");
            }
            board.getPiece(target).ifPresent(piece -> {
                if (piece.getPieceClass().isKing()) {
                    throw new IllegalStateException("A simple move can never directly capture a King!");
                }
            });
            Optional<Piece<P>> capturedPiece = board.getPiece(target).map(t -> board.clearPiece(target));
            Piece<P> movedPiece = board.movePiece(source, target);

            List<MoveResult.MovedPiece<C, P>> history = new ArrayList<>();
            history.add(MoveResult.MovedPiece.of(movedPiece, source, target));
            capturedPiece.ifPresent(p -> MoveResult.MovedPiece.of(p, target, null));
            return () -> history;
        };
    }

    @Override
    public String toString() {
        return player + "[" + source + "->" + target + "]";
    }

    @Override
    public int compareTo(SimpleMove<C> o) {
        if (o == this) return 0;
        int a = this.source.compareTo(o.source);
        if (a != 0) return a;
        return this.target.compareTo(o.target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMove<?> that = (SimpleMove<?>) o;

        if (!source.equals(that.source)) return false;
        if (!target.equals(that.target)) return false;
        return player == that.player;

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }
}
