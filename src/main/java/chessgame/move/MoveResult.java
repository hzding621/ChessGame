package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents the change a move has made
 */
public interface MoveResult<C extends Cell, P extends PieceClass> {

    Collection<MovedPiece<C, P>> getMovedPieces();

    class MovedPiece<C extends Cell, P extends PieceClass> {

        private final Piece<P> movedPiece;
        private final C source;
        private final C target;

        private MovedPiece(Piece<P> movedPiece, C source, C target) {
            this.movedPiece = movedPiece;
            this.source = source;
            this.target = target;
        }

        public static <C extends Cell, P extends PieceClass> MovedPiece<C, P> of(
                Piece<P> movedPiece, C source, C target) {
            return new MovedPiece<>(movedPiece, source, target);
        }

        public C getSource() {
            return source;
        }

        public Optional<C> getTarget() {
            return Optional.ofNullable(target);
        }

        public Piece<P> getPiece() {
            return movedPiece;
        }
    }
}