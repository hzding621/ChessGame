package chessgame.game;

import chessgame.board.Cell;
import chessgame.move.TransitionResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of PieceInformation
 */
public final class PieceInformationImpl<C extends Cell, P extends PieceClass> implements PieceInformation<C, P>{

    private final Map<Piece<P>, Integer> moveCounts = new HashMap<>();
    private final Map<Player, C> kingPosition = new HashMap<>();

    public PieceInformationImpl(Map<Player, C> kingStartingPosition) {
        kingPosition.putAll(kingStartingPosition);
    }

    public void updateInformation(TransitionResult<C, P> history) {
        history.getMovedPieces().forEach(movedPiece -> {
            moveCounts.put(movedPiece.getPiece(), moveCounts.getOrDefault(movedPiece.getPiece(), 0) + 1);
            if (movedPiece.getPiece().getPieceClass().isKing()) {
                if (!movedPiece.getTarget().isPresent()) {
                    throw new IllegalStateException("King should never have been captured!");
                }
                kingPosition.put(movedPiece.getPiece().getPlayer(), movedPiece.getTarget().get());
            }
        });
    }

    @Override
    public int getPieceMoveCount(Piece<P> piece) {
        return moveCounts.getOrDefault(piece, 0);
    }

    @Override
    public C locateKing(Player player) {
        return kingPosition.get(player);
    }
}
