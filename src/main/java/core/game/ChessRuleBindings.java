package core.game;

import core.board.ChessBoardViewer;
import core.board.Square;
import core.piece.PieceClass;
import core.rule.RuleBindings;


/**
 * Customized rule bindings. Provides a builder interface
 */
public final class ChessRuleBindings<P extends PieceClass> extends RuleBindings<Square, P, ChessBoardViewer<P>> {

    public interface Provider<P extends PieceClass> extends RuleBindings.Supplier<Square, P, ChessBoardViewer<P>, RuleBindings<Square, P, ChessBoardViewer<P>>> {}
}
