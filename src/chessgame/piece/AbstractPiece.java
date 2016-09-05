package chessgame.piece;

import chessgame.board.Cell;
import chessgame.player.Player;

/**
 * Abstract class for piece implementation to extend from. Contains basic information that are shared by all pieces.
 */
public abstract class AbstractPiece<C extends Cell, A extends PieceType>
        implements Piece<A> {

    private final A pieceClass;
    private final Player player;
    private final int id;

    public AbstractPiece(A pieceClass, Player player, int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPiece<?, ?> that = (AbstractPiece<?, ?>) o;

        if (id != that.id) return false;
        if (!pieceClass.equals(that.pieceClass)) return false;
        return player == that.player;

    }

    @Override
    public int hashCode() {
        int result = pieceClass.hashCode();
        result = 31 * result + player.hashCode();
        result = 31 * result + id;
        return result;
    }
}
