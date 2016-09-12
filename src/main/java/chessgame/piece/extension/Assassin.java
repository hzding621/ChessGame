package chessgame.piece.extension;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.StepSize;
import chessgame.board.GridViewer;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.piece.PieceRule;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An assassin moves just like the queen, but attack an opponent only if there is exactly one piece in between the cannon and the enemy
 */

public final class Assassin<C extends Cell, P extends PieceClass, D extends Direction<D>,
        B extends GridViewer<C, D, P>> implements PieceRule<C,P,B> {

    @Override
    public Collection<C> attacking(B board, C position, Player player) {
        Set<C> attacked = new HashSet<>();
        board.getEveryDirections().forEach(direction -> board.firstTwoEncounters(position, direction, StepSize.of(1, 0))
                .ifPresent(attack -> attacked.add(attack.second())));
        return attacked;
    }

    @Override
    public Collection<C> basicMoves(B board, C position, Player player) {
        return Stream.concat(
                attacking(board, position, player).stream()
                        .filter(c ->  board.isEnemy(c, player)),
                board.getEveryDirections().stream().flatMap(direction ->
                        board.travelUntilBlocked(position, direction, StepSize.of(1, 0), false, false).stream()
                                .filter(board::isEmpty))
        ).collect(Collectors.toList());

    }
}