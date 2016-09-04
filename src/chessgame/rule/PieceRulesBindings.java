package chessgame.rule;

import chessgame.board.*;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents mappings from a piece type to the rule associated with it on a certain type of board
 */
public final class PieceRulesBindings<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    private final Map<A, PieceRule<C, A, P, B>> ruleBindings = new HashMap<>();

    public boolean addRule(A pieceType, PieceRule<C, A, P, B> rule) {
        return ruleBindings.putIfAbsent(pieceType, rule) == null;
    }

    public PieceRule<C, A, P, B> getRule(A pieceType) {
        return ruleBindings.get(pieceType);
    }
}
