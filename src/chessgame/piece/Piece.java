package chessgame.piece;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.player.Player;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Represents an abstract chess piece that belongs to a piece type. Should be board-ignorant.
 * Different concrete piece types should inherit this type.
 */
public interface Piece<A extends PieceType> {

    A getPieceClass();

    Player getPlayer();

    int getId();


}
