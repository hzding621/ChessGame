package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.GridViewer;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.PieceInformation;
import chessgame.piece.*;

/**
 * Inherit the empty rule bindings to include standard chess pieces rule mappings
 */
public final class ChessRuleBindings<B extends GridViewer<Square, TwoDimension, StandardPieces>>
        extends RuleBindings<Square, StandardPieces, B>
        implements RequiresBoardView<Square, StandardPieces, B>, RequiresPieceInformation<Square, StandardPieces> {

    private final B chessBoard;
    private final PieceInformation<Square, StandardPieces> pieceInformation;

    public ChessRuleBindings(B chessBoard,
                             PieceInformation<Square, StandardPieces> pieceInformation)
    {
        this.chessBoard = chessBoard;
        this.pieceInformation = pieceInformation;

        addRule(StandardPieces.PAWN, new Pawn.PawnRule<>(chessBoard, pieceInformation));
        addRule(StandardPieces.KNIGHT, new Knight.KnightRule<>(chessBoard));
        addRule(StandardPieces.BISHOP, new Bishop.BishopRule<>(chessBoard));
        addRule(StandardPieces.ROOK, new Rook.RookRule<>(chessBoard));
        addRule(StandardPieces.QUEEN, new Queen.QueenRule<>(chessBoard));
        addRule(StandardPieces.KING, new King.KingRule<>(chessBoard, pieceInformation));
    }

    public B getBoardViewer() {
        return chessBoard;
    }

    @Override
    public PieceInformation<Square, StandardPieces> getPieceInformation() {
        return pieceInformation;
    }
}
