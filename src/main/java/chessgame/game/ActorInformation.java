package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceType;
import chessgame.player.Player;
import chessgame.rule.Pin;
import chessgame.rule.Rules;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains information computed from actor's pieces
 */
public class ActorInformation<C extends Cell, P extends PieceType, B extends BoardViewer<C, P>> {
    private final Map<C, Set<C>> availableMoves = new TreeMap<>();

    /**
     * This method recompute the entire actor information, which includes all available for the actor.
     * This also allows to check whether the actor has been checkmated, i.e. there are no valid moves for the actor
     * This method runs after every move is made and the defender information has been updated.
     */
    public void refresh(B board, Rules<C, P, B> rules,
                        DefenderInformation<C, P, B> defenderInformation,
                        PlayerInformation playerInformation,
                        C actorKing) {
        Player actor = playerInformation.getActor();
        Player defender = playerInformation.getDefender();
        availableMoves.clear();

        board.getAllPiecesForPlayer(actor)
            .stream()
            .forEach(actorLocator -> {

                // Get the king-pinners of the actor piece
                Collection<Pin<C>> kingDefendings = defenderInformation.isKingDefender(actorLocator.getCell());

                // Get all potential moves for the actor piece
                Set<C> moves = rules.basicMoves(board, actorLocator.getCell(), actor)
                    .stream()
                    .filter(targetCell ->
                        /* Either the piece is King and it is moving to a non-attacked position (can always do that)
                         * Or the move removes all poses check (but might expose the King, will be filtered below)
                         */
                        actorLocator.getPiece().getPieceClass().isKing()
                                ? !defenderInformation.isAttacked(targetCell)
                                : rules.canRemoveCheckThreats(targetCell, defender, actorKing, defenderInformation)
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
                    .collect(Collectors.toCollection(TreeSet::new));

                // Register filtered result
                if (!moves.isEmpty()) {
                    availableMoves.put(actorLocator.getCell(), moves);
                }
            });
    }

    /**
     * @return Mapping of available moves for each actor's pieces
     */
    public Map<C, Set<C>> getAvailableMoves() {
        return availableMoves;
    }
}
