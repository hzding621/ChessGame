package chessgame.move;

import chessgame.board.Cell;
import chessgame.game.ChessRuleBindings;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents the change a move has made
 */
public interface TransitionResult<C extends Cell, P extends PieceClass> {

    /**
     * This is a helper method so as that creation of this interface can utilize Java 8 lambda expression
     */
    static <C extends Cell, P extends PieceClass> TransitionResult<C, P> create(
            Supplier<List<MovedPiece<C, P>>> movedPieces, Supplier<Move<C, P>> reverseMove) {

        return new TransitionResult<C, P>() {
            @Override
            public List<MovedPiece<C, P>> getMovedPieces() {
                return movedPieces.get();
            }

            @Override
            public Move<C, P> getReverseMove() {
                return reverseMove.get();
            }
        };
    }

    /**
     * @return all moved pieces as a result of this move
     */
    List<MovedPiece<C, P>> getMovedPieces();

    /**
     * @return the move to reverse the effect of the just applied transition
     */
    Move<C, P> getReverseMove();

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