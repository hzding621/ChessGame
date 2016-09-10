package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.piece.PieceClass;
import chessgame.piece.StandardPieces;
import chessgame.rule.BasicRuleBindings;
import chessgame.rule.RuleBindings;

/**
 * Customized rule bindings. Provides a builder interface
 */
public class CustomRuleBindings<P extends PieceClass> extends RuleBindings<Square, P, ChessBoardViewer<P>> {

    public interface Supplier<P extends PieceClass> extends RuleBindings.Supplier<Square, P, ChessBoardViewer<P>, RuleBindings<Square, P, ChessBoardViewer<P>>> {}
}
