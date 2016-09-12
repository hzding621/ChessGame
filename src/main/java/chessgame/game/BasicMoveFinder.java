package chessgame.game;

import chessgame.board.Tile;
import chessgame.board.Board;
import chessgame.board.Previewable;
import chessgame.move.AnnotatedSimpleMove;
import chessgame.move.Move;
import chessgame.piece.Piece;
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
public final class BasicMoveFinder<C extends Tile, P extends PieceClass, M extends Board<C, P>,
        B extends Previewable<C, P, B, M>> implements MoveFinder<C, P>
{
    private final SetMultimap<C, Move<C, P>> availableMoves = MultimapBuilder.treeKeys().hashSetValues().build();

    private final B board;
    private final Rules<C, P, B> rules;
    private final RuntimeInformation<C, P> runtimeInformation;

    public BasicMoveFinder(B board, Rules<C, P, B> rules, RuntimeInformation<C, P> runtimeInformation) {
        this.board = board;
        this.rules = rules;
        this.runtimeInformation = runtimeInformation;
    }

    @Override
    public void recompute() {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        availableMoves.clear();

        board.getPiecesForPlayer(actor).forEach(sourcePosition -> availableMoves.putAll(sourcePosition,
                computeAvailableMoves(sourcePosition)));
    }


    public Collection<Move<C, P>> computeAvailableMoves(C sourcePosition) {

        Player actor = runtimeInformation.getPlayerInformation().getActor();
        Player defender = runtimeInformation.getPlayerInformation().getDefender();
        Piece<P> sourcePiece = board.getPiece(sourcePosition).get();
        Collection<Move<C, P>> moves = rules.basicMoves(board, sourcePosition, actor)
                .stream()
                .filter(targetPosition -> board.getPiece(targetPosition).map(p -> p.getPieceClass().canCapture()).orElse(true))
                .map(targetPosition -> AnnotatedSimpleMove.of(sourcePiece, sourcePosition, targetPosition, actor))
                .filter(move -> {

                    // "Preview the board situation after applying the move and gather the attacked position
                    Set<C> isAttacked = board.preview(move.getTransition(), future ->
                            future.getPiecesForPlayer(defender).stream()
                                    .flatMap(defenderPosition ->
                                            rules.attacking(future, defenderPosition, defender).stream())
                                    .collect(Collectors.toSet()));

                    // If King has moved, then king's target position cannot be attacked
                    // If King stayed, then king's position cannot be attacked
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
    public SetMultimap<C, Move<C, P>> getAvailableMoves() {
     return availableMoves;
    }

}
