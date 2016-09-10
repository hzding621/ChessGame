package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.game.AttackInformation;
import chessgame.game.RuntimeInformation;
import chessgame.piece.King;
import chessgame.piece.StandardPieces;

/**
 * Inherit the basic rule bindings to include every standard chess piece rule mapping, including castling, en passant, promotion, etc.
 */
public final class StandardRuleBindings extends BasicRuleBindings<ChessBoardViewer<StandardPieces>> {

    public StandardRuleBindings(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
        super(runtimeInformation);

        // Overwrite King's rule with one with Castling activated
        bindRule(StandardPieces.KING, new King.WithCastling(runtimeInformation));

        // TODO: pawn promotion, en passant
    }

    public interface Supplier extends RuleBindings.Supplier<Square, StandardPieces, ChessBoardViewer<StandardPieces>, StandardRuleBindings> {}
}