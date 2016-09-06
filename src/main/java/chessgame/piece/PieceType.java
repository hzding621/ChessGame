package chessgame.piece;

/**
 * Each Piece instance contains an instance of PieceType.
 * This interface is used to bind Piece classes to their associated PieceRule classes
 */
public interface PieceType {

    /**
     * @return whether this piece should be recognized as King in this game
     */
    boolean isKing();
}
