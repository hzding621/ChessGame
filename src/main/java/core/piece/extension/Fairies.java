package core.piece.extension;

import core.board.Tile;
import core.board.Direction;
import core.board.GridViewer;
import core.board.StepSize;
import core.piece.CompositePiece;
import core.piece.Knight;
import core.piece.PieceClass;
import core.piece.Leaper;
import core.piece.PieceRule;
import core.piece.Queen;
import core.piece.Rider;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

/**
 * Contains a bunch of standardized fairy chess pieces
 */
public final class Fairies {

    public static final class Camel<C extends Tile, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements Leaper<C, P, D, B> {

        @Override
        public StepSize getDistance() {
            return StepSize.of(1, 3);
        }
    }

    public static final class Zebra<C extends Tile, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements Leaper<C, P, D, B> {

        @Override
        public StepSize getDistance() {
            return StepSize.of(2, 3);
        }
    }

    public static final class Elephant<C extends Tile, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements Leaper<C, P, D, B> {

        @Override
        public StepSize getDistance() {
            return StepSize.of(2, 2);
        }
    }

    public static final class NightRider<C extends Tile, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements Rider<C, P, D, B> {

        @Override
        public Collection<D> getDirections(B board) {
            return board.getOrthogonalDirections();
        }

        @Override
        public StepSize getStepSize() {
            return StepSize.of(1, 2);
        }
    }

    public static final class Unicorn<C extends Tile, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> implements CompositePiece<C, P, B> {

        @Override
        public Collection<? extends PieceRule<C, P, B>> attackLike() {
            return ImmutableList.of(new Knight<>(), new Queen<>());
        }

        @Override
        public Collection<? extends PieceRule<C, P, B>> moveLike() {
            return ImmutableList.of(new Knight<>(), new Queen<>());
        }
    }
}