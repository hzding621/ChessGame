package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.Square;
import chessgame.game.DefenderInformation;
import chessgame.game.PieceInformation;
import chessgame.piece.Bishop;
import chessgame.piece.King;
import chessgame.piece.Knight;
import chessgame.piece.Pawn;
import chessgame.piece.Queen;
import chessgame.piece.Rook;
import chessgame.piece.StandardPieces;

/**
 * Inherit the empty rule bindings to include all standard chess pieces rule mappings
 */
public final class StandardRuleBindings extends RuleBindings<Square, StandardPieces, ChessBoard>
        implements
        RequiresBoardView<Square, StandardPieces, ChessBoard>,
        RequiresPieceInformation<Square, StandardPieces>,
        RequiresDefenderInformation<Square, StandardPieces, ChessBoard> {

    private final ChessBoard chessBoard;
    private final PieceInformation<Square, StandardPieces> pieceInformation;
    private final DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation;

    public StandardRuleBindings(ChessBoard chessBoard,
                                PieceInformation<Square, StandardPieces> pieceInformation,
                                DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation) {
        this.chessBoard = chessBoard;
        this.pieceInformation = pieceInformation;
        this.defenderInformation = defenderInformation;

        addRule(StandardPieces.PAWN, new Pawn.PawnRule<>(chessBoard, pieceInformation));
        addRule(StandardPieces.KNIGHT, new Knight.KnightRule<>(chessBoard));
        addRule(StandardPieces.BISHOP, new Bishop.BishopRule<>(chessBoard));
        addRule(StandardPieces.ROOK, new Rook.RookRule<>(chessBoard));
        addRule(StandardPieces.QUEEN, new Queen.QueenRule<>(chessBoard));
        addRule(StandardPieces.KING, new King.KingRuleWithCastling(chessBoard, pieceInformation, defenderInformation));
    }

    public ChessBoard getBoardViewer() {
        return chessBoard;
    }

    @Override
    public PieceInformation<Square, StandardPieces> getPieceInformation() {
        return pieceInformation;
    }

    @Override
    public DefenderInformation<Square, StandardPieces, ChessBoard> getDefenderInformation() {
        return defenderInformation;
    }
}
