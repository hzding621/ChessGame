package chessgame.move;

import chessgame.board.Cell;
import chessgame.board.Board;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a move that involves a single piece
 */
public class SimpleMove<C extends Cell, P extends PieceClass> implements Move<C, P>, Comparable<SimpleMove<C, P>> {

    private final C source;
    private final C target;
    private final Player player;

    protected SimpleMove(C source, C target, Player player) {
        this.source = source;
        this.target = target;
        this.player = player;
    }

    public static <C extends Cell, P extends PieceClass> SimpleMove<C, P> of(C source, C target, Player player) {
        return new SimpleMove<>(source, target, player);
    }

    @Override
    public C getInitiator() {
        return source;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public C getTarget() {
        return target;
    }

    @Override
    public <M extends Board<C, P>> BoardTransition<C, P, M> getTransition() {
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

            List<TransitionResult.MovedPiece<C, P>> history = new ArrayList<>();
            capturedPiece.ifPresent(p -> history.add(TransitionResult.MovedPiece.of(p, target, null)));
            history.add(TransitionResult.MovedPiece.of(movedPiece, source, target));
            return TransitionResult.create(() -> history, () -> new ReverseMove<>(this, history));
        };
    }

    @Override
    public String toString() {
        return player + "[" + source + "->" + target + "]";
    }

    @Override
    public int compareTo(SimpleMove<C, P> o) {
        if (o == this) return 0;
        int a = this.source.compareTo(o.source);
        if (a != 0) return a;
        int b = this.target.compareTo(o.target);
        if (b == 0) throw new IllegalStateException("Two identical moves performed by different players should never appear at the same time");
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SimpleMove)) return false;

        SimpleMove<?, ?> that = (SimpleMove<?, ?>) o;

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
