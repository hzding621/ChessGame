package chessgame;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.rule.BoardInformation;
import chessgame.rule.Rules;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    B getBoard();

    BoardInformation<C, A, P> getBoardInformation();

    Rules<C, A, P, B> getRule();

    Collection<Player> getPlayers();

    Collection<C> allMoves(Player player);

    void makeMove(Move<C, A, P, B> move);
}
