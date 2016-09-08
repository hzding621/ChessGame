package chessgame.rule;

import chessgame.board.ChessBoard;
import chessgame.board.Square;
import chessgame.game.DefenderInformation;
import chessgame.game.PieceInformation;
import chessgame.piece.King;
import chessgame.piece.StandardPieces;

/**
 * Inherit the empty rule bindings to include all standard chess pieces rule mappings
 */
public final class StandardRuleBindings extends BasicRuleBindings<ChessBoard>
        implements
        RequiresBoardView<Square, StandardPieces, ChessBoard>,
        RequiresPieceInformation<Square, StandardPieces>,
        RequiresDefenderInformation<Square, StandardPieces, ChessBoard> {

    private final DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation;

    public StandardRuleBindings(ChessBoard chessBoard,
                                PieceInformation<Square, StandardPieces> pieceInformation,
                                DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation) {
        super(chessBoard, pieceInformation);
        this.defenderInformation = defenderInformation;

        // Overwrite King's rule with one with Castling activated
        bindRule(StandardPieces.KING, new King.KingRuleWithCastling(chessBoard, pieceInformation, defenderInformation));
    }

    @Override
    public DefenderInformation<Square, StandardPieces, ChessBoard> getDefenderInformation() {
        return defenderInformation;
    }
}