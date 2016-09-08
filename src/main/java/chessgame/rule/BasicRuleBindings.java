package chessgame.rule;

import chessgame.board.GridViewer;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.PieceInformation;
import chessgame.piece.*;

/**
 * Inherit the empty rule bindings to include basic chess pieces rule mappings (not including castling)
 */
public final class BasicRuleBindings<B extends GridViewer<Square, TwoDimension, StandardPieces>>
        extends RuleBindings<Square, StandardPieces, B>
        implements RequiresBoardView<Square, StandardPieces, B>, RequiresPieceInformation<Square, StandardPieces> {

    private final B gridBoard;
    private final PieceInformation<Square, StandardPieces> pieceInformation;

    public BasicRuleBindings(B gridBoard,
                             PieceInformation<Square, StandardPieces> pieceInformation) {
        this.gridBoard = gridBoard;
        this.pieceInformation = pieceInformation;

        addRule(StandardPieces.PAWN, new Pawn.PawnRule<>(gridBoard, pieceInformation));
        addRule(StandardPieces.KNIGHT, new Knight.KnightRule<>(gridBoard));
        addRule(StandardPieces.BISHOP, new Bishop.BishopRule<>(gridBoard));
        addRule(StandardPieces.ROOK, new Rook.RookRule<>(gridBoard));
        addRule(StandardPieces.QUEEN, new Queen.QueenRule<>(gridBoard));
        addRule(StandardPieces.KING, new King.KingRule<>(gridBoard, pieceInformation));
    }

    public B getBoardViewer() {
        return gridBoard;
    }

    @Override
    public PieceInformation<Square, StandardPieces> getPieceInformation() {
        return pieceInformation;
    }
}
