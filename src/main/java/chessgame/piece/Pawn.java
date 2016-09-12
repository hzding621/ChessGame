package chessgame.piece;

import chessgame.board.Tile;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.game.RuntimeInformation;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;
import utility.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that implements Pawn piece moving logic
 */

public final class Pawn<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>>
        implements OptimizedPiece<C, P, B>, RequiresRuntimeInformation<C, P>, PieceRule<C,P,B> {

    private final RuntimeInformation<C, P> runtimeInformation;

    public Pawn(RuntimeInformation<C, P> runtimeInformation) {
        super();
        this.runtimeInformation = runtimeInformation;
    }

    private Collection<C> initialMove(B board, C position, Player player) {
        if (runtimeInformation.getPieceInformation().getPieceMoveCount(board.getPiece(position).get()) == 0) {
            return CollectionUtils.asArrayList(board.travelForwardBlockable(position, player)
                    .flatMap(oneMove -> board.travelForwardBlockable(oneMove, player)));
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<C> attacking(B board, C position, Player player) {
        return board.attackPawnStyle(position, player);
    }

    @Override
    public Collection<C> basicMoves(B board, C position, Player player) {
        List<C> list = CollectionUtils.asArrayList(board.travelForwardBlockable(position, player));
        list.addAll(initialMove(board, position, player));
        list.addAll(attacking(board, position, player).stream()
                .filter(c -> board.isEnemy(c, player))
                .collect(Collectors.toList()));
        return list;
    }

    @Override
    public Collection<C> attackBlockingPositions(B board, C sourcePosition,
                                                 C targetPosition,
                                                 Player player) {
        if (!attacking(board, sourcePosition, player).contains(targetPosition)) {
            throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
        }

        // To block a pawn attack, can only capture pawn (or move away attacked piece)
        return ImmutableList.of(sourcePosition);
    }

    @Override
    public RuntimeInformation<C, P> getRuntimeInformation() {
        return runtimeInformation;
    }
}