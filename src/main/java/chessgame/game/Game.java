package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.PieceClass;
import chessgame.rule.Rules;
import chessgame.player.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    B getBoard();

    BoardInformation<C, P, B> getBoardInformation();

    Rules<C, P, B> getRule();

    Player getActor();

    Player getDefender();

    Collection<Move<C>> availableMoves();

    Collection<Move<C>> availableMovesFrom(C c);

    void move(Move<C> move);

    GameStatus getGameStatus();
}
