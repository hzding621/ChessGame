package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.move.Move;
import chessgame.move.TransitionResult;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import chessgame.rule.Rules;
import chessgame.rule.StandardRuleBindings;

import java.util.Collection;

/**
 * The standard chess game
 */
public final class StandardGame implements Game<Square, StandardPieces, ChessBoard> {
    private final ChessBoard chessBoard;
    private final Rules<Square, StandardPieces, ChessBoardViewer> chessRules;
    private final RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer> runtimeInformation;
    private final MoveFinder<Square, StandardPieces> moveFinder;
    private GameStatus gameStatus = GameStatus.OPEN;


    private StandardGame(ChessBoard chessBoard,
                         Rules<Square, StandardPieces, ChessBoardViewer> chessRules,
                         RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer> runtimeInformation,
                         MoveFinder<Square, StandardPieces> moveFinder) {
        this.chessBoard = chessBoard;
        this.chessRules = chessRules;
        this.runtimeInformation = runtimeInformation;
        this.moveFinder = moveFinder;
        runtimeInformation.initializeInformation(chessRules);
        moveFinder.recompute();
    }

    public static StandardGame constructGame() {
        StandardSetting pieceSet = new StandardSetting();
        ChessBoard board = ChessBoard.create(pieceSet);
        RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer> runtimeInformation =
                new RuntimeInformationImpl<>(pieceSet, board);
        Rules<Square, StandardPieces, ChessBoardViewer> rules =
                new Rules<>(new StandardRuleBindings(runtimeInformation));
        MoveFinder<Square, StandardPieces> moveFinder = new BasicMoveFinder<>(board, rules, runtimeInformation);
        return new StandardGame(board, rules, runtimeInformation, moveFinder);
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public Player getActor() {
        return runtimeInformation.getPlayerInformation().getActor();
    }

    @Override
    public Player getDefender() {
        return runtimeInformation.getPlayerInformation().getDefender();
    }

    @Override
    public Collection<Move<Square>> availableMoves() {
        return moveFinder.getAvailableMoves().values();
    }

    @Override
    public Collection<Move<Square>> availableMovesFrom(Square square) {
        return moveFinder.getAvailableMoves().get(square);
    }

    private void updateInformationForThisRound(TransitionResult<Square, StandardPieces> history) {
        // Update everything in runtime information
        runtimeInformation.updateInformationForThisRound(chessRules, history);
        moveFinder.recompute();

        // Update Checkmate/Stalemate situation
        if (moveFinder.getAvailableMoves().isEmpty()) {
            if (runtimeInformation.getAttackInformation().getCheckers().isEmpty()) {
                gameStatus = GameStatus.STALEMATE;
            } else {
                gameStatus =  GameStatus.CHECKMATE;
            }
        }
        gameStatus = GameStatus.OPEN;
    }

    @Override
    public void move(Move<Square> attemptedMove) {
        if (gameStatus != GameStatus.OPEN) {
            throw new IllegalStateException("Game has ended in " + gameStatus);
        }
        Square source = attemptedMove.getInitiator();
        if (!moveFinder.getAvailableMoves().get(source).contains(attemptedMove)) {
            throw new IllegalStateException("Attempted move " + attemptedMove + " is invalid!");
        }
        updateInformationForThisRound(chessBoard.apply(attemptedMove.getTransition()));
        moveFinder.recompute();
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
