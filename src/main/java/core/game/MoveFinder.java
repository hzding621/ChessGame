package core.game;

import core.board.Tile;
import core.move.Move;
import core.piece.PieceClass;
import com.google.common.collect.SetMultimap;

/**
 * Contains information computed from actor's pieces
 */
public interface MoveFinder<C extends Tile, P extends PieceClass> {

    /**
     * Recompute the available moves. Should be triggered after each move has been made.
     * It should check whether the actor has been checkmated, i.e. there are no valid moves for the actor
     * And any valid move it returns should not put king into a directly attacked position.
     */
    void recompute();

    /**
     * @return Mapping of available moves for each actor's pieces
     */
    SetMultimap<C, Move<C, P>> getAvailableMoves();
}
