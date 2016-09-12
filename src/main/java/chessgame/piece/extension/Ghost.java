package chessgame.piece.extension;

import chessgame.board.Tile;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.piece.PieceRule;
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

