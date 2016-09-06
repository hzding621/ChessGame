package chessgame.game;

import chessgame.board.Cell;
import chessgame.move.MoveResult;
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

    public void updateInformation(MoveResult<C, P> history) {
        history.getMovedPieces().stream().forEach(movedPiece -> {
            moveCounts.put(movedPiece.getPiece(), moveCounts.getOrDefault(movedPiece.getPiece(), 0) + 1);
            if (movedPiece.getPiece().getPieceClass().isKing()) {
                if (!movedPiece.getTarget().isPresent()) {
                    throw new IllegalStateException("King should never have been captured!");
                }
                kingPosition.put(movedPiece.getPiece().getPlayer(), movedPiece.getTarget().get());
            }
        });
    }

    public int getPieceMoveCount(Piece<P> piece) {
        return moveCounts.getOrDefault(piece, 0);
    }

    public C locateKing(Player player) {
        return kingPosition.get(player);
    }
}
