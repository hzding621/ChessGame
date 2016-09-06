package chessgame.game;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents runtime piece information such as how many moves it has made
 */
public final class PieceInformation<C extends Cell, P extends PieceClass> {

    private final Map<Piece<P>, Integer> moveCounts = new HashMap<>();
    private final Map<Player, C> kingPosition = new HashMap<>();

    public PieceInformation(Map<Player, C> kingStartingPosition) {
        kingPosition.putAll(kingStartingPosition);
    }

    public void incrementPieceMoveCount(Piece<P> piece) {
        moveCounts.put(piece, moveCounts.getOrDefault(piece, 0) + 1);
    }

    public int getPieceMoveCount(Piece<P> piece) {
        return moveCounts.getOrDefault(piece, 0);
    }

    public C locateKing(Player player) {
        return kingPosition.get(player);
    }

    public void updateKingPosition(Player player, C cell) {
        kingPosition.put(player, cell);
    }

}
