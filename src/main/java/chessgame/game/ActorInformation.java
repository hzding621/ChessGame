package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.piece.PieceClass;
import chessgame.rule.Rules;
import com.google.common.collect.SetMultimap;

/**
 * Contains information computed from actor's pieces
 */
public interface ActorInformation<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    /**
     * @return Mapping of available moves for each actor's pieces
     */
    SetMultimap<C, Move<C>> getAvailableMoves();
}
