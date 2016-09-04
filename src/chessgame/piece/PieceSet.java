package chessgame.piece;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Created by haozhending on 9/4/16.
 */
public interface PieceSet<C extends Cell, A extends PieceType, P extends Piece<A>> {

    Collection<A> getAllTypes();

    boolean doesIncludePiece(A type);

    int getPieceCountForOnePlayer(A type);

    Collection<C> getStartingPositions(A type, Player player);
}
