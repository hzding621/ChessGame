package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents the change a move has made
 */
public interface TransitionResult<C extends Cell, P extends PieceClass> {

    /**
     * @return all moved pieces as a result of this move
     */
    Collection<MovedPiece<C, P>> getMovedPieces();

    /**
     * Annotated piece that includes its soruce position and its optional target position
     * If the piece is taken out of the board, target is empty
     * If the piece appears from nowhere (for example in case of Pawn promotion), source is empty
     */
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

        public Optional<C> getSource() {
            return Optional.ofNullable(source);
        }

        public Optional<C> getTarget() {
            return Optional.ofNullable(target);
        }

        public Piece<P> getPiece() {
            return movedPiece;
        }
    }
}