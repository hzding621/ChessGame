package chessgame.game;

import chessgame.board.Cell;
import chessgame.board.MutableBoard;
import chessgame.board.Previewer;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Rules;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Find moves by brute-force
 */
public final class BasicMoveFinder<C extends Cell, P extends PieceClass, M extends MutableBoard<C, P, M>,
        B extends Previewer<C, P, B, M>> implements MoveFinder<C, P>
{
    private final SetMultimap<C, Move<C>> availableMoves = MultimapBuilder.treeKeys().hashSetValues().build();

    private final B board;
    private final Rules<C, P, B> rules;
    private final RuntimeInformation<C, P> runtimeInformation;

    public BasicMoveFinder(B board, Rules<C, P, B> rules, RuntimeInformation<C, P> runtimeInformation) {
        this.board = board;
        this.rules = rules;
        this.runtimeInformation = runtimeInformation;
    }

    /**
     * This method recompute the entire actor information, which includes all available moves for the actor.
     * This also allows to check whether the actor has been checkmated, i.e. there are no valid moves for the actor
     * This method runs after every move is made and the defender information has been updated.
     */
    @Override
    public void recompute() {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        availableMoves.clear();

        board.getPiecesForPlayer(actor).forEach(sourcePosition -> availableMoves.putAll(sourcePosition,
                computeAvailableMoves(sourcePosition)));
    }


    public Collection<Move<C>> computeAvailableMoves(C sourcePosition) {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();
        Collection<Move<C>> moves = rules.basicMoves(board, sourcePosition, actor)
                .stream()
                .map(targetPosition -> SimpleMove.of(sourcePosition, targetPosition, actor))
                .filter(move -> {
                    B future = board.preview(move.getTransition());
                    Set<C> isAttacked = future.getPiecesForPlayer(defender).stream()
                            .flatMap(defenderPosition ->
                                    rules.attacking(future, defenderPosition, defender).stream())
                            .collect(Collectors.toSet());
                    C kingStartingPosition = runtimeInformation.getPieceInformation().locateKing(actor);
                    boolean result;
                    if (sourcePosition.equals(kingStartingPosition)) {
                        // King moved
                        result = !isAttacked.contains(move.getTarget());
                    } else {
                        result = !isAttacked.contains(kingStartingPosition);
                    }
                    return result;
                })
                .collect(Collectors.toList());
        moves.addAll(rules.specialMove(board, sourcePosition, actor));
        return moves;
    }

    @Override
    public SetMultimap<C, Move<C>> getAvailableMoves() {
     return availableMoves;
    }

}