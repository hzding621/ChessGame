package core.game;

import core.board.ChessBoard;
import core.board.ChessBoardViewer;
import core.board.Square;
import core.piece.StandardPieces;
import core.rule.Rules;
import core.rule.StandardRuleBindings;

/**
 * A standard with 8x8 chessboard and standard pieces
 */
public final class StandardGame extends ChessGame<StandardPieces> {

    private StandardGame(ChessBoard<StandardPieces> chessBoard,
                         GameSetting.GridGame<Square, StandardPieces> gameSetting,
                         Rules<Square, StandardPieces, ChessBoardViewer<StandardPieces>> chessRules,
                         RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer<StandardPieces>> runtimeInformation,
                         MoveFinder<Square, StandardPieces> moveFinder) {

        super(chessBoard, gameSetting, chessRules, runtimeInformation, moveFinder);
    }

    public static StandardGame create() {
        ChessBoard<StandardPieces> board = ChessBoard.create(StandardSetting.VALUE);
        RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer<StandardPieces>> runtimeInformation =
                new RuntimeInformationImpl<>(StandardSetting.VALUE, board);
        Rules<Square, StandardPieces, ChessBoardViewer<StandardPieces>> rules =
                new Rules<>(new StandardRuleBindings(runtimeInformation));
        MoveFinder<Square, StandardPieces> moveFinder = new OptimizedMoveFinder<>(board, rules, runtimeInformation);
        return new StandardGame(board, StandardSetting.VALUE, rules, runtimeInformation, moveFinder);
    }
}
