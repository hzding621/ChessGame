package chessgame.rule;

import chessgame.board.GridViewer;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.PieceInformation;
import chessgame.game.RuntimeInformation;
import chessgame.piece.Bishop;
import chessgame.piece.King;
import chessgame.piece.Knight;
import chessgame.piece.Pawn;
import chessgame.piece.Queen;
import chessgame.piece.Rook;
import chessgame.piece.StandardPieces;

/**
 * Inherit the empty rule bindings to include basic chess pieces rule mappings (not including castling)
 */
public class BasicRuleBindings<B extends GridViewer<Square, TwoDimension, StandardPieces>>
        extends RuleBindings<Square, StandardPieces, B> {

    public BasicRuleBindings(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
        bindRule(StandardPieces.PAWN, new Pawn.PawnRule<>(runtimeInformation));
        bindRule(StandardPieces.KNIGHT, new Knight.KnightRule<>());
        bindRule(StandardPieces.BISHOP, new Bishop.BishopRule<>());
        bindRule(StandardPieces.ROOK, new Rook.RookRule<>());
        bindRule(StandardPieces.QUEEN, new Queen.QueenRule<>());
        bindRule(StandardPieces.KING, new King.KingRule<>(runtimeInformation));
    }
}
