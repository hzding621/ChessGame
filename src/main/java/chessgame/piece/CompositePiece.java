package chessgame.piece;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A piece that has exactly the same power as its components
 */
public interface CompositePiece<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        extends PieceRule<C, P, B> {

    Collection<? extends PieceRule<C, P, B>> attackLike();

    Collection<? extends PieceRule<C, P, B>> moveLike();

    @Override
    default Collection<C> attacking(B board, C position, Player player) {
        return attackLike().stream()
                .flatMap(piece -> piece.attacking(board, position, player).stream())
                .collect(Collectors.toSet());
    }

    @Override
    default Collection<C> basicMoves(B board, C position, Player player) {
        return moveLike().stream()
                .flatMap(piece -> piece.basicMoves(board, position, player).stream())
                .collect(Collectors.toSet());
    }
}



