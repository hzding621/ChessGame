package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.SquareCell;
import chessgame.piece.ChessPieceType;
import chessgame.piece.Piece;
import chessgame.rule.ChessRuleBindings;
import chessgame.rule.Rules;

/**
 * Contains tests for ActorInformation
 * Mainly tests for Available Moves computation
 */
public final class ActorInformationTest {

    private ChessBoard testBoard;
    private Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> rules;
    private BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> boardInformation;

    private void hydrate(GameSetting<SquareCell, ChessPieceType, Piece<ChessPieceType>> customizedSet) {
        boardInformation = new BoardInformation<>(customizedSet);
        testBoard = new ChessBoard(customizedSet);
        rules = new Rules<>(new ChessRuleBindings(testBoard, boardInformation.getPieceInformation()));
        boardInformation.getDefenderInformation().refresh(testBoard, rules, boardInformation.getPlayerInformation(),
                boardInformation.getPieceInformation().locateKing(customizedSet.starter()));
    }

}
