package chessgame.game;

import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a certain kind of chess piece set with board configuration
 */
public interface GameSetting<C extends Cell, P extends PieceClass> {

    /**
     * @return all starting pieces in this game setting by starting position
     */
    Map<C, Piece<P>> constructAllPiecesByStartingPosition();

    /**
     * @return all supported piece types in this game
     */
    Collection<P> getSupportedTypes();

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

    interface GridGame<C extends Cell, P extends PieceClass> extends GameSetting<C, P> {

        int getRankLength();

        int getFileLength();
    }
}
