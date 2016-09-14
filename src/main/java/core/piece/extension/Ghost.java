package core.piece.extension;

import core.board.Tile;
import core.board.Direction;
import core.board.GridViewer;
import core.piece.PieceClass;
import core.player.Player;
import core.piece.PieceRule;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A ghost can teleport to any unoccupied position on the board, and can never be captured.
 * Also never captures any opponent pieces.
 * For now it requires a grid board so that it can enumerate all positions on the board
 */

public final class Ghost<C extends Tile, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> implements PieceRule<C,P,B> {

    @Override
    public Collection<C> attacking(B board, C position, Player player) {
        return ImmutableList.of();
    }

    @Override
    public Collection<C> basicMoves(B board, C position, Player player) {
        return board.getAllPositions().stream().filter(board::isEmpty).collect(Collectors.toList());
    }
}

