package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Represents a move that involves a single piece
 */
public final class SimpleMove<C extends Cell> implements Move<C>, Comparable<SimpleMove<C>> {

    private final C source;
    private final C target;
    private final Player player;

    private SimpleMove(C source, C target, Player player) {
        this.source = source;
        this.target = target;
        this.player = player;
    }

    public static <C extends Cell> SimpleMove<C> of(C source, C target, Player player) {
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
        int b = this.target.compareTo(o.target);
        if (b == 0) throw new IllegalStateException("Two identical moves performed by different players should never appear at the same time");
        return b;
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
