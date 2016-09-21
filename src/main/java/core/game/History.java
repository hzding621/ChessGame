package core.game;

import core.board.Square;
import core.board.Tile;
import core.move.TransitionResult;
import core.piece.PieceClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the history of this game
 */
public class History<C extends Tile, P extends PieceClass> {

    private final List<TransitionResult<C, P>> transitionResults = new ArrayList<>();

    public void pushNewResult(TransitionResult<C, P> transitionResult) {
        transitionResults.add(transitionResult);
    }

    public TransitionResult<C, P> popLastResult() {
        TransitionResult<C, P> transitionResult = transitionResults.get(transitionResults.size() - 1);
        transitionResults.remove(transitionResults.size() - 1);
        return transitionResult;
    }

    public int getHistorySize() {
        return transitionResults.size();
    }
}
