package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;
import chessgame.rule.Rules;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import java.util.*;

/**
 * Default implementation of Actor Information
 */
public class ActorInformationImpl<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements ActorInformation<C, P, B> {
    private final SetMultimap<C, Move<C>> availableMoves = MultimapBuilder.treeKeys().hashSetValues().build();

    /**
     * This method recompute the entire actor information, which includes all available moves for the actor.
     * This also allows to check whether the actor has been checkmated, i.e. there are no valid moves for the actor
     * This method runs after every move is made and the defender information has been updated.
     */
    public void refresh(B board,
                        Rules<C, P, B> rules,
                        PlayerInformation playerInformation,
                        DefenderInformation<C, P, B> defenderInformation) {

        Player actor = playerInformation.getActor();
        availableMoves.clear();

        board.getAllPiecesForPlayer(actor)
                .stream()
                .forEach(sourcePosition -> availableMoves.putAll(sourcePosition,
                        rules.computeAvailableMoves(board, sourcePosition, actor, defenderInformation)));
    }

    @Override
    public SetMultimap<C, Move<C>> getAvailableMoves() {
        return availableMoves;
    }
}
