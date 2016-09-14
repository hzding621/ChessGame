package core.rule;

import core.board.ChessBoardViewer;
import core.board.Square;
import core.game.RuntimeInformation;
import core.piece.King;
import core.piece.StandardPieces;

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
}