package chessgame.move;

import chessgame.board.Cell;
import chessgame.board.Board;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Move to reverse the effect of a simple move or castling move
 */
public class ReverseMove<C extends Cell, P extends PieceClass> implements Move<C, P> {

    private final Move<C, P> originalMove;
    private final List<TransitionResult.MovedPiece<C, P>> movedPieces;

    public ReverseMove(Move<C, P> originalMove, List<TransitionResult.MovedPiece<C, P>> movedPieces) {
        this.originalMove = originalMove;
        this.movedPieces = Lists.reverse(movedPieces);
    }

    @Override
    public C getInitiator() {
        throw new UnsupportedOperationException("Reverse move does not have an initiator!");
    }

    @Override
    public Player getPlayer() {
        return originalMove.getPlayer();
    }

    @Override
    public <M extends Board<C, P>> BoardTransition<C, P, M> getTransition() {
        return board -> {
            movedPieces.forEach(movedPiece -> {
                if (movedPiece.getSource().isPresent() && movedPiece.getTarget().isPresent()) {
                    board.movePiece(movedPiece.getTarget().get(), movedPiece.getSource().get());
                } else if (movedPiece.getSource().isPresent()) {
                    board.addPiece(movedPiece.getSource().get(), movedPiece.getPiece());
                } else {
                    board.clearPiece(movedPiece.getTarget().get());
                }
            });

            return TransitionResult.create(() -> movedPieces.stream()
                    .map(movedPiece -> TransitionResult.MovedPiece.of(movedPiece.getPiece(),
                            movedPiece.getTarget().orElseGet(null), movedPiece.getSource().orElseGet(null)))
                    .collect(Collectors.toList()), () -> originalMove);
        };
    }
}
