package chessgame.piece;

import chessgame.player.Player;

/**
 * Abstract class for piece implementation to extend from. Contains basic information that are shared by all pieces.
 */
public final class PieceImpl<P extends PieceClass> implements Piece<P> {

    private final P pieceClass;
    private final Player player;
    private final int id;

    public PieceImpl(P pieceClass, Player player, int id) {
        this.pieceClass = pieceClass;
        this.player = player;
        this.id = id;
    }

    @Override
    public P getPieceClass() {
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
    public String toString() {
        // e.g. WHITE[KNIGHT,1]

        return player + "[" + pieceClass + "," + id + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceImpl<?> that = (PieceImpl<?>) o;

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
