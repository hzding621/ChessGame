package chessgame.game;

import chessgame.board.BoardView;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.PinnedSet;
import chessgame.rule.Rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains information computed from actor's pieces
 */
public class ActorInformation<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardView<C, A, P>> {
    private final Map<C, Set<C>> availableMoves = new HashMap<>();

    public void refresh(B board, Rules<C, A, P, B> rules,
                        DefenderInformation<C, A, P, B> defenderInformation,
                        PlayerInformation playerInformation,
                        C actorKing) {
        Player actor = playerInformation.getActor();
        availableMoves.clear();

        board.getAllPiecesForPlayer(actor)
            .parallelStream()
            .forEach(actorPiece -> {

                // Get the king-pinners of the actor piece
                Collection<PinnedSet<C>> kingDefendings = defenderInformation.isKingDefender(actorPiece.getCell());

                // Get all potential moves for the actor piece
                Set<C> moves = rules.basicMoves(board, actorPiece.getCell(), actor)
                        .parallelStream()
                        .filter(targetCell ->
                    /*
                     * Either the piece is King and it is moving to a non-attacked position (can always do that)
                     * Or the move removes all poses check (but might expose the King, will be filtered below)
                     */
                                        actorPiece.getPiece().getPieceClass().isKing() && !defenderInformation.isAttacked(targetCell)
                                                || rules.canRemoveCheckThreats(targetCell, board, actor, actorKing, defenderInformation)
                        )

                /*
                 * Actor piece cannot be a king defenders (king defenders cannot move)
                 * Unless this move removes all of its pinner(s) (usually can allow only one pinner)
                 * Filter condition translation:
                 *    For all pinning situation, pinning.attacker == targetCell
                 */
                        .filter(targetCell -> !kingDefendings.parallelStream()
                                .anyMatch(pin -> !pin.getAttacker().equals(targetCell))
                        )
                        .collect(Collectors.toSet());

                availableMoves.put(actorPiece.getCell(), moves);
            });
    }

    public Map<C, Set<C>> getAvailableMoves() {
        return availableMoves;
    }
}
