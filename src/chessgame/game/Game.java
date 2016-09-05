package chessgame.game;

import chessgame.board.BoardView;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.rule.Rules;
import chessgame.player.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardView<C, A, P>> {

    B getBoard();

    BoardInformation<C, A, P, B> getBoardInformation();

    Rules<C, A, P, B> getRule();

    Player getActor();

    Player getDefender();

    Map<C, Set<C>> getAllMoves();

    default Set<C> getMoves(C c) {
        return getAllMoves().getOrDefault(c, Collections.EMPTY_SET);
    }

    void makeMove(C source, C target);
}
