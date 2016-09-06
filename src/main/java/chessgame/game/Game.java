package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.PieceClass;
import chessgame.rule.Rules;
import chessgame.player.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Interface for a variant of chess game
 */
interface Game<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    B getBoard();

    BoardInformation<C, P, B> getBoardInformation();

    Rules<C, P, B> getRule();

    Player getActor();

    Player getDefender();

    Map<C, Set<C>> allPotentialMovesBySource();

    default Collection<Move<C>> allPotentialMoves() {
        return allPotentialMovesBySource().entrySet()
                .stream()
                .map(e -> e.getValue()
                        .stream()
                        .map(target -> SimpleMove.of(e.getKey(), target)))
                .flatMap(Function.identity())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    default Set<C> allPotentialMovesOf(C c) {
        return allPotentialMovesBySource().getOrDefault(c, Collections.emptySet());
    }

    void move(C source, C target);

    GameStatus getGameStatus();
}
