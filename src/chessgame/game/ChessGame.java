package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.SquareCell;
import chessgame.piece.*;
import chessgame.player.Player;
import chessgame.rule.RuleBindings;
import chessgame.rule.Rules;

import java.util.*;

/**
 * The standard chess game
 */
public class ChessGame implements Game<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> {
    private final ChessBoard chessBoard;
    private final Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> chessRules;
    private final BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> boardInformation;

    private ChessGame(ChessBoard chessBoard,
                      Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> chessRules,
                      BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> boardInformation) {
        this.chessBoard = chessBoard;
        this.chessRules = chessRules;
        this.boardInformation = boardInformation;
    }

    public static ChessGame constructGame() {
        PieceSet<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceSet = new ChessPieceSet();
        ChessBoard chessBoard = new ChessBoard(pieceSet);
        chessBoard.initializeBoard();

        RuleBindings<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> ruleBindings = new RuleBindings<>();

        ruleBindings.addRule(ChessPieceType.PAWN, new Pawn.PawnRule<>());
        ruleBindings.addRule(ChessPieceType.KNIGHT, new Knight.KnightRule<>());
        ruleBindings.addRule(ChessPieceType.BISHOP, new Bishop.BishopRule<>());
        ruleBindings.addRule(ChessPieceType.ROOK, new Rook.RookRule<>());
        ruleBindings.addRule(ChessPieceType.QUEEN, new Queen.QueenRule<>());
        ruleBindings.addRule(ChessPieceType.KING, new King.KingRule<>());

        return new ChessGame(chessBoard, new Rules<>(ruleBindings),
                new BoardInformation<>(pieceSet));
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> getBoardInformation() {
        return boardInformation;
    }

    @Override
    public Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> getRule() {
        return chessRules;
    }

    @Override
    public Player getActor() {
        return boardInformation.getActor();
    }

    @Override
    public Player getDefender() {
        return boardInformation.getDefender();
    }

    @Override
    public Map<SquareCell, Set<SquareCell>> getAllMoves() {
        return boardInformation.getAvailableMoves();
    }

    @Override
    public void makeMove(SquareCell source, SquareCell target) {
        if (!boardInformation.getAvailableMoves().get(source).contains(target)) {
            throw new IllegalStateException("Invalid move from " + source + " to " + target);
        }
        chessBoard.clearPiece(target);
        chessBoard.movePiece(source, target);

        // Update King's position
        if (source.equals(boardInformation.getKingPosition().get(boardInformation.getActor()))) {
            boardInformation.getKingPosition().put(boardInformation.getActor(), target);
        }

        boardInformation.getPlayerInformation().nextRound();

        // Defender information is used to compute actor information so must be computed earlier
        boardInformation.getDefenderInformation().refresh(chessBoard, chessRules,
                boardInformation.getPlayerInformation(), boardInformation.getKingPosition(boardInformation.getActor()));
        boardInformation.getActorInformation().refresh(chessBoard, chessRules, boardInformation.getDefenderInformation(),
                boardInformation.getPlayerInformation(), boardInformation.getKingPosition(boardInformation.getActor()));

        if (boardInformation.getAvailableMoves().isEmpty()) {
            boardInformation.endGame();
        }
    }

    public static void main(String[] args) {
        ChessGame game = ChessGame.constructGame();
    }
}
