package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.SquareCell;
import chessgame.game.PieceInformation;
import chessgame.piece.*;

/**
 * Inherit the empty rule bindings to include standard chess pieces rule mappings
 */
public final class ChessRuleBindings extends RuleBindings<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard>
implements
        RequiresBoardView<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard>,
        RequiresPieceInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> {

    private final ChessBoard chessBoard;
    private final PieceInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceInformation;

    public ChessRuleBindings(ChessBoard chessBoard,
                             PieceInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceInformation)
    {
        this.chessBoard = chessBoard;
        this.pieceInformation = pieceInformation;

        addRule(ChessPieceType.PAWN, new Pawn.PawnRule<>(chessBoard, pieceInformation));
        addRule(ChessPieceType.KNIGHT, new Knight.KnightRule<>(chessBoard));
        addRule(ChessPieceType.BISHOP, new Bishop.BishopRule<>(chessBoard));
        addRule(ChessPieceType.ROOK, new Rook.RookRule<>(chessBoard));
        addRule(ChessPieceType.QUEEN, new Queen.QueenRule<>(chessBoard));
        addRule(ChessPieceType.KING, new King.KingRule<>(chessBoard, pieceInformation));
    }

    public ChessBoard getBoardViewer() {
        return chessBoard;
    }

    @Override
    public PieceInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> getPieceInformation() {
        return pieceInformation;
    }
}
