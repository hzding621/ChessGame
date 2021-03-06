package core.game;

import core.board.Tile;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a certain kind of chess piece set with board configuration
 */
public interface GameSetting<C extends Tile, P extends PieceClass> {

    /**
     * @return all starting pieces in this game setting by starting position
     */
    Map<C, Piece<P>> constructPiecesByStartingPosition();

    /**
     * @return starting position of kings for each player
     */
    Map<Player, C> getKingStartingPositions();

    /**
     * TODO: refactor this so that the library provides better support for more than 2 players
     * @return Get all players in this game
     */
    Collection<Player> getPlayers();

    /**
     * @return The first player to make move in this game
     */
    Player getStarter();

    interface GridGame<C extends Tile, P extends PieceClass> extends GameSetting<C, P> {

        int getRankLength();

        int getFileLength();
    }
}
