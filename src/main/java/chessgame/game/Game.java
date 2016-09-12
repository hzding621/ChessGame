package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>, S extends GameSetting<C, P>> {

    B getBoard();

    Player getActor();

    Player getDefender();

    Collection<Move<C>> availableMoves();

    Collection<Move<C>> availableMovesFrom(C c);

    void move(Move<C> move);

    GameStatus getGameStatus();

    S getSetting();
}
