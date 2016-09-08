package chessgame.move;

import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

/**
 * Represent the castling move in standard chess game
 */
public final class Castling<C extends Cell> implements CompositeMove<C> {

    private final SimpleMove<C> kingMove;
    private final SimpleMove<C> rookMove;

    public Castling(SimpleMove<C> kingMove, SimpleMove<C> rookMove) {
        if (!kingMove.getPlayer().equals(rookMove.getPlayer())) {
            throw new IllegalStateException("King's move and rook's move have different players.");
        }
        this.kingMove = kingMove;
        this.rookMove = rookMove;
    }

    @Override
    public C actorKingNewPosition() {
        return kingMove.getTarget();
    }

    @Override
    public C getInitiator() {
        return kingMove.getInitiator();
    }

    @Override
    public Player getPlayer() {
        return kingMove.getPlayer();
    }

    @Override
    public <P extends PieceClass> BoardTransition<C, P> getTransition() {
        return board -> {
            Piece<P> king = board.movePiece(kingMove.getInitiator(), kingMove.getTarget());
            Piece<P> rook = board.movePiece(rookMove.getInitiator(), rookMove.getTarget());
            return () -> ImmutableList.of(
                    MoveResult.MovedPiece.of(king, kingMove.getInitiator(), kingMove.getTarget()),
                    MoveResult.MovedPiece.of(rook, rookMove.getInitiator(), rookMove.getTarget()));
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Castling<?> castling = (Castling<?>) o;

        if (!kingMove.equals(castling.kingMove)) return false;
        return rookMove.equals(castling.rookMove);

    }

    @Override
    public int hashCode() {
        int result = kingMove.hashCode();
        result = 31 * result + rookMove.hashCode();
        return result;
    }
}
