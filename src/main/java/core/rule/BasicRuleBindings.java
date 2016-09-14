package core.rule;

import core.board.GridViewer;
import core.board.Square;
import core.board.TwoDimension;
import core.game.RuntimeInformation;
import core.piece.Bishop;
import core.piece.King;
import core.piece.Knight;
import core.piece.Pawn;
import core.piece.Queen;
import core.piece.Rook;
import core.piece.StandardPieces;

/**
 * Inherit the empty rule bindings to include basic chess pieces rule mappings (not including castling)
 */
public class BasicRuleBindings<B extends GridViewer<Square, TwoDimension, StandardPieces>>
        extends RuleBindings<Square, StandardPieces, B> {

    public BasicRuleBindings(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
        bindRule(StandardPieces.PAWN, new Pawn<>(runtimeInformation));
        bindRule(StandardPieces.KNIGHT, new Knight<>());
        bindRule(StandardPieces.BISHOP, new Bishop<>());
        bindRule(StandardPieces.ROOK, new Rook<>());
        bindRule(StandardPieces.QUEEN, new Queen<>());
        bindRule(StandardPieces.KING, new King<>(runtimeInformation));
    }
}
