package chessgame;

import chessgame.board.Board;
import chessgame.move.Move;
import chessgame.rule.Rule;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Created by haozhending on 9/3/16.
 */
interface Game {

    Board getBoard();

    Rule getRule();

    Collection<Player> getPlayers();

    void makeMove(Move move);
}
