package core.move;

import core.board.Tile;
import core.board.Board;
import core.piece.PieceClass;
import core.player.Player;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Move to reverse the effect of a simple move or castling move
 */
final class ReverseMove<C extends Tile, P extends PieceClass> implements Move<C, P> {

    private final Move<C, P> originalMove;
    private final List<TransitionResult.MovedPiece<C, P>> movedPieces;

    ReverseMove(Move<C, P> originalMove, List<TransitionResult.MovedPiece<C, P>> movedPieces) {
        this.originalMove = originalMove;
        this.movedPieces = Lists.reverse(movedPieces);
    }

    @Override
    public C getInitiator() {
        throw new UnsupportedOperationException("Reverse move does not have an initiator!");
    }

    @Override
    public C getDestination() {
        throw new UnsupportedOperationException("Reverse move does not have a destination!");
    }

    @Override
    public Player getPlayer() {
        return originalMove.getPlayer();
    }

    @Override
    public <M extends Board<C, P>> BoardTransition<C, P, M> getTransition() {
        return board -> {
            movedPieces.forEach(movedPiece -> {
                if (movedPiece.getSource().isPresent() && movedPiece.getDestination().isPresent()) {
                    board.movePiece(movedPiece.getDestination().get(), movedPiece.getSource().get());
                } else if (movedPiece.getSource().isPresent()) {
                    board.addPiece(movedPiece.getSource().get(), movedPiece.getPiece());
                } else {
                    board.clearPiece(movedPiece.getDestination().get());
                }
            });

            return TransitionResult.create(() -> movedPieces.stream()
                    .map(movedPiece -> TransitionResult.MovedPiece.of(movedPiece.getPiece(),
                            movedPiece.getDestination().orElse(null), movedPiece.getSource().orElse(null)))
                    .collect(Collectors.toList()), () -> originalMove);
        };
    }
}
