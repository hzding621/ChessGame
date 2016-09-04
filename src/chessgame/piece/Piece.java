package chessgame.piece;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.Collection;


/**
 * Represents an abstract chess piece that belongs to a piece type. Should be board-ignorant.
 * Different concrete piece types should inherit this type.
 */
public interface Piece<B extends Board, C extends Cell> {

    Player getPlayer();

    int getId();

    CaptureRule<B, C> getRule();

    /**
     * Abstraction of moving rule for the piece class,
     * handles piece moving logic by passing in board information.
     */
    interface CaptureRule<B extends Board, C extends Cell> {

        default Collection<C> attacking(C position, B board, Player player){
            return getPossibleMoves(board, position, player);
        }

        Collection<C> getPossibleMoves(B board, C position, Player player);
    }
}
