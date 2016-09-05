package chessgame.piece;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.board.SquareCell;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a certain kind of chess piece set with board configuration
 */
public interface PieceSet<C extends Cell, A extends PieceType, P extends Piece<A>> {

    Collection<A> getAllTypes();

    Map<Player, C> getKingStartingPositions();

    boolean doesIncludePiece(A type);

    int getPieceCountForOnePlayer(A type);

    Collection<C> getStartingPositions(A type, Player player);


}
