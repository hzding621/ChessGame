package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.Direction;
import chessgame.board.Distance;
import chessgame.board.GridViewer;
import chessgame.player.Player;
import chessgame.rule.PieceRule;

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
        board.getAllDirections().forEach(direction -> {
            board.firstAndSecondOccupant(position, direction, Distance.of(1, 0))
                    .ifPresent(attack -> attacked.add(attack.second()));
        });
        return attacked;
    }

    @Override
    public Collection<C> basicMoves(B board, C position, Player player) {
        return Stream.concat(
                attacking(board, position, player).stream()
                        .filter(c ->  board.isEnemy(c, player)),
                board.getAllDirections().stream().flatMap(direction ->
                        board.furthestReach(position, direction, Distance.of(1, 0), false, false).stream()
                                .filter(board::isEmpty))
        ).collect(Collectors.toList());

    }
}