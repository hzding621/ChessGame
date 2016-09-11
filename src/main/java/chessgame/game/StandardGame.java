package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.piece.StandardPieces;
import chessgame.rule.Rules;
import chessgame.rule.StandardRuleBindings;

/**
 * A standard with 8x8 chessboard and standard pieces
 */
public class StandardGame extends ChessGame<StandardPieces> {

    private StandardGame(ChessBoard<StandardPieces> chessBoard,
                         Rules<Square, StandardPieces, ChessBoardViewer<StandardPieces>> chessRules,
                         RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer<StandardPieces>> runtimeInformation,
                         MoveFinder<Square, StandardPieces> moveFinder) {

        super(chessBoard, chessRules, runtimeInformation, moveFinder);
    }

    public static StandardGame create() {
        StandardSetting setting = new StandardSetting();
        ChessBoard<StandardPieces> board = ChessBoard.create(setting);
        RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer<StandardPieces>> runtimeInformation =
                new RuntimeInformationImpl<>(setting, board);
        Rules<Square, StandardPieces, ChessBoardViewer<StandardPieces>> rules =
                new Rules<>(new StandardRuleBindings(runtimeInformation));
        MoveFinder<Square, StandardPieces> moveFinder = new OptimizedMoveFinder<>(board, rules, runtimeInformation);
        return new StandardGame(board, rules, runtimeInformation, moveFinder);
    }
}
