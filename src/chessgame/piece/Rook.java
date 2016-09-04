package chessgame.piece;

import chessgame.board.Direction;
import chessgame.board.GridBoard;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements Rook piece moving logic
 */
public final class Rook<C extends Cell, D extends Direction> extends AbstractPiece<GridBoard<C, D>, C> {

    private final CaptureRule<GridBoard<C, D>, C> rule = (board, position, player) -> {
        List<C> moveTos = new ArrayList<>();
        board.getDiagonalDirections().forEach(direction ->
                board.furthestReachWithCapture(direction, position, player).getPath().forEach(moveTos::add));
        return moveTos;
    };

    public Rook(Player player, int id) {
        super(player, id);
    }

    @Override
    public CaptureRule<GridBoard<C, D>, C> getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "Rook{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
}