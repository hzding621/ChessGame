package core.move;

import core.board.Tile;
import core.board.Board;
import core.piece.Piece;
import core.piece.PieceClass;
import core.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Represent the castling move in standard chess game
 */
public final class CastlingMove<C extends Tile, P extends PieceClass> implements Move<C, P> {

    private final SimpleMove<C, P> kingMove;
    private final SimpleMove<C, P> rookMove;

    public CastlingMove(SimpleMove<C, P> kingMove, SimpleMove<C, P> rookMove) {
        if (!kingMove.getPlayer().equals(rookMove.getPlayer())) {
            throw new IllegalStateException("King's move and rook's move have different players.");
        }
        this.kingMove = kingMove;
        this.rookMove = rookMove;
    }

    @Override
    public C getInitiator() {
        return kingMove.getInitiator();
    }

    @Override
    public C getDestination() {
        // Castling move highlights the rook as the destination
        return rookMove.getInitiator();
    }

    @Override
    public Player getPlayer() {
        return kingMove.getPlayer();
    }

    @Override
    public <M extends Board<C, P>> BoardTransition<C, P, M> getTransition() {
        return board -> {
            Piece<P> king = board.movePiece(kingMove.getInitiator(), kingMove.getTarget());
            Piece<P> rook = board.movePiece(rookMove.getInitiator(), rookMove.getTarget());
            List<TransitionResult.MovedPiece<C, P>> history = ImmutableList.of(
                    TransitionResult.MovedPiece.of(king, kingMove.getInitiator(), kingMove.getTarget()),
                    TransitionResult.MovedPiece.of(rook, rookMove.getInitiator(), rookMove.getTarget()));

            return TransitionResult.create(() -> history, () -> new ReverseMove<>(this, history));
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CastlingMove<?, ?> castlingMove = (CastlingMove<?, ?>) o;

        return kingMove.equals(castlingMove.kingMove) && rookMove.equals(castlingMove.rookMove);

    }

    @Override
    public int hashCode() {
        int result = kingMove.hashCode();
        result = 31 * result + rookMove.hashCode();
        return result;
    }
}
