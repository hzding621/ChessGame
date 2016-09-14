package core.piece;

/**
 * Each Piece instance contains an instance of PieceClass.
 * This interface is used to bind Piece classes to their associated PieceRule classes
 */
public interface PieceClass {

    /**
     * @return whether this piece should be recognized as King in this game
     */
    boolean isKing();
    
    /**
     * @return whether this type of piece can be captured
     */
    boolean canCapture();
}
