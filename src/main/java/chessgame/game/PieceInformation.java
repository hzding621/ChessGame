package chessgame.game;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents runtime piece information such as how many moves it has made
 */
public final class PieceInformation<C extends Cell, A extends PieceType, P extends Piece<A>> {

    private final Map<P, Integer> moveCounts = new HashMap<>();
    private final Map<Player, C> kingPosition = new HashMap<>();

    public PieceInformation(Map<Player, C> kingStartingPosition) {
        kingPosition.putAll(kingStartingPosition);
    }

    public void incrementPieceMoveCount(P piece) {
        moveCounts.put(piece, moveCounts.getOrDefault(piece, 0) + 1);
    }

    public int getPieceMoveCount(P piece) {
        return moveCounts.getOrDefault(piece, 0);
    }

    public C locateKing(Player player) {
        return kingPosition.get(player);
    }

    public void updateKingPosition(Player player, C cell) {
        kingPosition.put(player, cell);
    }

}
