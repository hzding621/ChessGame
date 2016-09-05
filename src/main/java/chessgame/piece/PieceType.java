package chessgame.piece;

/**
 * Each Piece instance contains an instance of PieceType.
 * This interface is used to bind Piece classes to their associated PieceRule classes
 */
public interface PieceType {

    boolean isKing();
}
