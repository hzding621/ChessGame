package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.GridCellFactory;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.move.Move;
import chessgame.piece.*;
import chessgame.player.Player;
import chessgame.rule.ChessRuleBindings;
import chessgame.rule.Rules;

import java.util.*;

/**
 * The standard chess game
 */
public class StandardGame implements Game<Square, StandardPieces, ChessBoard> {
    private final ChessBoard chessBoard;
    private final Rules<Square, StandardPieces, ChessBoard> chessRules;
    private final BoardInformation<Square, StandardPieces, ChessBoard> boardInformation;
    private GameStatus gameStatus = GameStatus.OPEN;

    private StandardGame(ChessBoard chessBoard,
                         Rules<Square, StandardPieces, ChessBoard> chessRules,
                         BoardInformation<Square, StandardPieces, ChessBoard> boardInformation) {
        this.chessBoard = chessBoard;
        this.chessRules = chessRules;
        this.boardInformation = boardInformation;
        refreshInformation();
    }

    public static StandardGame constructGame() {
        StandardSetting pieceSet = new StandardSetting();
        BoardInformation<Square, StandardPieces, ChessBoard> boardInformation =
                new BoardInformation<>(pieceSet);
        ChessBoard board = new ChessBoard(pieceSet);
        ChessRuleBindings ruleBindings = new ChessRuleBindings(board, boardInformation.getPieceInformation());
        return new StandardGame(board, new Rules<>(ruleBindings), boardInformation);
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public BoardInformation<Square, StandardPieces, ChessBoard> getBoardInformation() {
        return boardInformation;
    }

    @Override
    public Rules<Square, StandardPieces, ChessBoard> getRule() {
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
    public Map<Square, Set<Square>> allPotentialMovesBySource() {
        return boardInformation.getAvailableMoves();
    }

    private void refreshInformation() {
        // Defender information is used to compute actor information so must be computed earlier
        boardInformation.getDefenderInformation().refresh(chessBoard, chessRules,
                boardInformation.getPlayerInformation(), boardInformation.locateKing(boardInformation.getActor()));
        boardInformation.getActorInformation().refresh(chessBoard, chessRules, boardInformation.getDefenderInformation(),
                boardInformation.getPlayerInformation(), boardInformation.locateKing(boardInformation.getActor()));

        // Update Checkmate/Stalemate situation
        if (boardInformation.getAvailableMoves().isEmpty()) {
            if (boardInformation.getDefenderInformation().getCheckers().isEmpty()) {
                gameStatus = GameStatus.CHECKMATE;
            } else {
                gameStatus = GameStatus.STALEMATE;
            }
        }
    }

    @Override
    public void move(Square source, Square target) {
        if (gameStatus != GameStatus.OPEN) {
            throw new IllegalStateException("Game has ended in " + gameStatus);
        }
        if (!boardInformation.getAvailableMoves().getOrDefault(source, Collections.emptySet()).contains(target)) {
            throw new IllegalStateException("Invalid move from " + source + " to " + target);
        }
        chessBoard.getPiece(target).ifPresent(p -> {
            if (p.getPieceClass().isKing()) {
                throw new IllegalStateException("Can never directly capture a King!");
            }
        });
        chessBoard.clearPiece(target);
        Piece<StandardPieces> movedPiece = chessBoard.movePiece(source, target);

        // Increment move count for moved piece
        boardInformation.getPieceInformation().incrementPieceMoveCount(movedPiece);

        // Update King's position
        if (source.equals(boardInformation.locateKing(boardInformation.getActor()))) {
            boardInformation.moveKing(boardInformation.getActor(), target);
        }

        boardInformation.getPlayerInformation().nextRound();

        refreshInformation();
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public static void main(String[] args) {
        StandardGame game = StandardGame.constructGame();

        GridCellFactory<Square, TwoDimension> factory = game.getBoard().getGridCellFactory();
        String[][] moves = new String[][] {
                {"D", "2", "D", "4"},
                {"D", "7", "D", "5"},
                {"E", "2", "E", "4"},
                {"D", "5", "E", "4"},
                {"F", "1", "B", "5"},
                {"C", "7", "C", "6"},
                {"B", "5", "C", "6"},
        };

        Collection<Move<Square>> allMoves = null;
        for (String[] move: moves) {
            allMoves = game.allPotentialMoves();
            game.move(factory.at(move[0], move[1]), factory.at(move[2], move[3]));
        }
        allMoves = game.allPotentialMoves();

        System.out.println("Game Over!");
    }

    private static final String[][][] testCases = {
            {
                    {"F", "2", "F", "3"},
                    {"E", "7", "E", "5"},
                    {"G", "2", "G", "4"},
                    {"D", "8", "H", "4"},
            },
    };
}
