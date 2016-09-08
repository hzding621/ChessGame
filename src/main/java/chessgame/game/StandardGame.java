package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.GridCellFactory;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.move.Move;
import chessgame.move.MoveResult;
import chessgame.move.SimpleMove;
import chessgame.piece.*;
import chessgame.player.Player;
import chessgame.rule.BasicRuleBindings;
import chessgame.rule.Rules;
import com.google.common.collect.ImmutableList;

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
        updateInformationForThisRound(ImmutableList::of, true);
    }

    public static StandardGame constructGame() {
        StandardSetting pieceSet = new StandardSetting();
        BoardInformation<Square, StandardPieces, ChessBoard> boardInformation =
                new BoardInformation<>(pieceSet);
        ChessBoard board = new ChessBoard(pieceSet);
        BasicRuleBindings ruleBindings = new BasicRuleBindings(board, boardInformation);
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
    public Collection<Move<Square>> availableMoves() {
        return boardInformation.getAvailableMoves().values();
    }

    @Override
    public Collection<Move<Square>> availableMovesFrom(Square square) {
        return boardInformation.getAvailableMoves().get(square);
    }

    private void updateInformationForThisRound(MoveResult<Square, StandardPieces> history, boolean opening) {
        // Update everthing in board information
        gameStatus = boardInformation.updateInformationForThisRound(chessBoard, chessRules, history, opening);
    }

    @Override
    public void move(Move<Square> attemptedMove) {
        if (gameStatus != GameStatus.OPEN) {
            throw new IllegalStateException("Game has ended in " + gameStatus);
        }
        Square source = attemptedMove.getInitiator();
        if (!boardInformation.getAvailableMoves().get(source).contains(attemptedMove)) {
            throw new IllegalStateException("Attempted move " + attemptedMove + " is invalid!");
        }
        updateInformationForThisRound(attemptedMove.<StandardPieces>getTransition().apply(chessBoard), false);
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
            allMoves = game.availableMoves();
            game.move(SimpleMove.of(factory.at(move[0], move[1]), factory.at(move[2], move[3]), game.getActor()));
        }
        allMoves = game.availableMoves();

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
