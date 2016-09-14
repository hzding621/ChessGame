package core.rule;

import core.board.BoardViewer;
import core.board.Tile;
import core.game.RuntimeInformation;
import core.piece.PieceClass;
import core.piece.PieceRule;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents mappings from a piece type to the rule associated with it on a certain type of board
 */
public abstract class RuleBindings<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final Map<P, PieceRule<C, P, B>> bindings = new HashMap<>();

    public void bindRule(P pieceType, PieceRule<C, P, B> rule) {
        bindings.put(pieceType, rule);
    }

    public PieceRule<C, P, B> getRule(P pieceType) {
        return bindings.get(pieceType);
    }

    public interface Supplier<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>, T extends RuleBindings<C, P, B>> {

        T get(RuntimeInformation<C, P> runtimeInformation);
    }
}
