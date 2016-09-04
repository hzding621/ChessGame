package chessgame.piece;

import chessgame.board.Cell;
import chessgame.player.Player;

/**
 * Created by haozhending on 9/4/16.
 */
public abstract class AbstractPiece<C extends Cell, A extends PieceType>
        implements Piece<A> {

    private final A pieceClass;

    private final Player player;
    private final int id;
    protected AbstractPiece(A pieceClass, Player player, int id) {
        this.pieceClass = pieceClass;
        this.player = player;
        this.id = id;
    }

    @Override
    public A getPieceClass() {
        return pieceClass;
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
