package core.game;

import core.board.BoardViewer;
import core.board.Tile;
import core.move.Move;
import core.piece.PieceClass;
import core.player.Player;

import java.util.Collection;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>, S extends GameSetting<C, P>> {

    B getBoard();

    Player getActor();

    Player getDefender();

    Collection<Move<C, P>> availableMoves();

    Collection<Move<C, P>> availableMovesFrom(C c);

    void move(Move<C, P> move);

    GameStatus getGameStatus();

    S getSetting();
}
