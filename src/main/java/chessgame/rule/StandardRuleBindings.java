package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.game.AttackInformation;
import chessgame.game.RuntimeInformation;
import chessgame.piece.King;
import chessgame.piece.StandardPieces;

/**
 * Inherit the empty rule bindings to include all standard chess pieces rule mappings
 */
public final class StandardRuleBindings extends BasicRuleBindings<ChessBoardViewer> {

    public StandardRuleBindings(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
        super(runtimeInformation);

        // Overwrite King's rule with one with Castling activated
        bindRule(StandardPieces.KING, new King.KingRuleWithCastling(runtimeInformation));
    }
}