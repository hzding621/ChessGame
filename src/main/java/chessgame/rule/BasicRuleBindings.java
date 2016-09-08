package chessgame.rule;

import chessgame.board.GridViewer;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.PieceInformation;
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
        extends RuleBindings<Square, StandardPieces, B>
        implements RequiresBoardView<Square, StandardPieces, B>, RequiresPieceInformation<Square, StandardPieces> {

    private final B gridBoard;
    private final PieceInformation<Square, StandardPieces> pieceInformation;

    public BasicRuleBindings(B gridBoard,
                             PieceInformation<Square, StandardPieces> pieceInformation) {
        this.gridBoard = gridBoard;
        this.pieceInformation = pieceInformation;

        bindRule(StandardPieces.PAWN, new Pawn.PawnRule<>(gridBoard, pieceInformation));
        bindRule(StandardPieces.KNIGHT, new Knight.KnightRule<>(gridBoard));
        bindRule(StandardPieces.BISHOP, new Bishop.BishopRule<>(gridBoard));
        bindRule(StandardPieces.ROOK, new Rook.RookRule<>(gridBoard));
        bindRule(StandardPieces.QUEEN, new Queen.QueenRule<>(gridBoard));
        bindRule(StandardPieces.KING, new King.KingRule<>(gridBoard, pieceInformation));
    }

    public B getBoardViewer() {
        return gridBoard;
    }

    @Override
    public PieceInformation<Square, StandardPieces> getPieceInformation() {
        return pieceInformation;
    }
}
