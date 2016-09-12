package chessgame.piece.extension;

import chessgame.board.Tile;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.StepSize;
import chessgame.piece.CompositePiece;
import chessgame.piece.Knight;
import chessgame.piece.PieceClass;
import chessgame.piece.Leaper;
import chessgame.piece.PieceRule;
import chessgame.piece.Queen;
import chessgame.piece.Rider;
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