package chessgame.rule;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.PieceClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents mappings from a piece type to the rule associated with it on a certain type of board
 */
public abstract class RuleBindings<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final Map<P, PieceRule<C, P, B>> bindings = new HashMap<>();

    public boolean addRule(P pieceType, PieceRule<C, P, B> rule) {
        return bindings.putIfAbsent(pieceType, rule) == null;
    }

    public PieceRule<C, P, B> getRule(P pieceType) {
        return bindings.get(pieceType);
    }
}
