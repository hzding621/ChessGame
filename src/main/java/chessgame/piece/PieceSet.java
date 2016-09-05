package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a certain kind of chess piece set with board configuration
 */
public interface PieceSet<C extends Cell, A extends PieceType, P extends Piece<A>> {

    default Collection<PieceLocator<C, A, P>> constructAllPieces() {
        return getSupportedTypes()
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
    }

    Collection<A> getSupportedTypes();

    Collection<PieceLocator<C, A, P>> constructPiecesOfTypeAndPlayer(A type, Player player);

    Map<Player, C> getKingStartingPositions();

    Collection<Player> getPlayers();
}
