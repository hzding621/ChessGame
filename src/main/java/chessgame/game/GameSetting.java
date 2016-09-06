package chessgame.game;

import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a certain kind of chess piece set with board configuration
 */
public interface GameSetting<C extends Cell, A extends PieceType, P extends Piece<A>> {

    /**
     * Create all pieces in this game setting. Used to populate the chess board
     * @return all starting pieces in this game setting
     */
    default Collection<PieceLocator<C, A, P>> constructAllPieces() {
        Collection<PieceLocator<C, A, P>> locators = getSupportedTypes()
                .stream()
                .map(type -> getPlayers()
                        .stream()
                        .map(player ->
                                constructPiecesOfTypeAndPlayer(type, player)
                                        .stream()

                        )
                        .flatMap(e -> e)
                )
                .flatMap(e -> e)
                .collect(Collectors.toList());
        return locators;
    }

    /**
     * @return all supported piece types in this game
     */
    Collection<A> getSupportedTypes();

    /**
     * Create pieces of certain type and certain player in this game setting. Used to populate the chess board
     * @return all PieceLocator of create pieces
     */
    Collection<PieceLocator<C, A, P>> constructPiecesOfTypeAndPlayer(A type, Player player);

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
    Player starter();
}