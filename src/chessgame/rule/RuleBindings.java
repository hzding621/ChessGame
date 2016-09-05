package chessgame.rule;

import chessgame.board.*;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents mappings from a piece type to the rule associated with it on a certain type of board
 */
public final class RuleBindings<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardView<C, A, P>> {

    private final Map<A, PieceRule<C, A, P, B>> bindings = new HashMap<>();

    public boolean addRule(A pieceType, PieceRule<C, A, P, B> rule) {
        return bindings.putIfAbsent(pieceType, rule) == null;
    }

    public PieceRule<C, A, P, B> getRule(A pieceType) {
        return bindings.get(pieceType);
    }
}
