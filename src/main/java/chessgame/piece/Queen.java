package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

/**
 * Class that implements Queen piece moving logic
 */
public final class Queen<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>>
        implements Rider<C, P, D, B>, PieceRule<C,P,B> {

    @Override
    public Collection<D> getDirections(B board) {
        return board.getEveryDirections();
    }

    /**
     * This version is implemented by using CompositePiece interface. It is not optimized and used for testing purpose.
     */
    static class Composite<C extends Cell, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements CompositePiece<C, P, B> {

        @Override
        public Collection<PieceRule<C, P, B>> attackLike() {
            return ImmutableList.of(new Bishop<>(), new Rook<>());
        }

        @Override
        public Collection<PieceRule<C, P, B>> moveLike() {
            return ImmutableList.of(new Bishop<>(), new Rook<>());
        }
    }
}

