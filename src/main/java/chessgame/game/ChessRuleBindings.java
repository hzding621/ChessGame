package chessgame.game;

import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.piece.PieceClass;
import chessgame.rule.RuleBindings;


/**
 * Customized rule bindings. Provides a builder interface
 */
public final class ChessRuleBindings<P extends PieceClass> extends RuleBindings<Square, P, ChessBoardViewer<P>> {

    public interface Provider<P extends PieceClass> extends RuleBindings.Supplier<Square, P, ChessBoardViewer<P>, RuleBindings<Square, P, ChessBoardViewer<P>>> {}
}
