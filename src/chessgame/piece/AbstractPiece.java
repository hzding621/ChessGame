package chessgame.piece;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.player.Player;

/**
 * Created by haozhending on 9/4/16.
 */
public abstract class AbstractPiece<B extends Board, C extends Cell> implements Piece<B, C> {

    private final Player player;
    private final int id;

    protected AbstractPiece(Player player, int id) {
        this.player = player;
        this.id = id;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getId() {
        return id;
    }
}
