package chessgame.rule;

import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Piece that is able to initiate a move that involes more than one
 */
public interface SpecialMovePieceRule<C extends Cell> {

    Collection<Move<C>> specialMove(Player player);
}
