package core.game;

import core.board.ChessBoard;
import core.board.ChessBoardViewer;
import core.board.Square;
import core.move.Move;
import core.move.TransitionResult;
import core.piece.PieceClass;
import core.player.Player;
import core.rule.Rules;

import java.util.Collection;

/**
 * Main class for implementing main loop in a chess game
 */
public class ChessGame<P extends PieceClass> implements Game<Square, P, ChessBoard<P>, GameSetting.GridGame<Square, P>> {

    private final ChessBoard<P> chessBoard;
    private final GameSetting.GridGame<Square, P> gameSetting;
    private final Rules<Square, P, ChessBoardViewer<P>> chessRules;
    private final RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation;
    private final MoveFinder<Square, P> moveFinder;
    private final History<Square, P> history = new History<>();
    private GameStatus gameStatus = GameStatus.OPEN;


    protected ChessGame(ChessBoard<P> chessBoard,
                        GameSetting.GridGame<Square, P> gameSetting,
                        Rules<Square, P, ChessBoardViewer<P>> chessRules,
                        RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation,
                        MoveFinder<Square, P> moveFinder) {
        this.chessBoard = chessBoard;
        this.gameSetting = gameSetting;
        this.chessRules = chessRules;
        this.runtimeInformation = runtimeInformation;
        this.moveFinder = moveFinder;
        runtimeInformation.initializeInformation(chessRules);
        moveFinder.recompute();
    }

    public static <P extends PieceClass> ChessGame<P> create(GameSetting.GridGame<Square, P> setting,
                                                             ChessRuleBindings.Provider<P> ruleBindingProvider) {
        ChessBoard<P> board = ChessBoard.create(setting);
        RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation =
                new RuntimeInformationImpl<>(setting, board);
        Rules<Square, P, ChessBoardViewer<P>> rules =
                new Rules<>(ruleBindingProvider.get(runtimeInformation));
        MoveFinder<Square, P> moveFinder = new BasicMoveFinder<>(board, rules, runtimeInformation);
        return new ChessGame<>(board, setting, rules, runtimeInformation, moveFinder);
    }

    @Override
    public ChessBoard<P> getBoard() {
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
    public Collection<Move<Square, P>> availableMoves() {
        return moveFinder.getAvailableMoves().values();
    }

    @Override
    public Collection<Move<Square, P>> availableMovesFrom(Square square) {
        return moveFinder.getAvailableMoves().get(square);
    }

    private void updateInformationForThisRound(TransitionResult<Square, P> transitionResult, boolean isUndo) {
        // Update everything in runtime information
        runtimeInformation.updateInformationForThisRound(chessRules, transitionResult, isUndo);
        moveFinder.recompute();
        if (!isUndo) {
            history.pushNewResult(transitionResult);
        }

        // Update Checkmate/Stalemate situation
        if (moveFinder.getAvailableMoves().isEmpty()) {
            if (runtimeInformation.getAttackInformation().getCheckers().isEmpty()) {
                gameStatus = GameStatus.STALEMATE;
            } else {
                gameStatus =  GameStatus.CHECKMATE;
            }
        } else {
            gameStatus = GameStatus.OPEN;
        }
    }

    @Override
    public void move(Move<Square, P> attemptedMove) {
        if (gameStatus != GameStatus.OPEN) {
            throw new IllegalStateException("Game has ended in " + gameStatus);
        }
        Square source = attemptedMove.getInitiator();
        if (!moveFinder.getAvailableMoves().get(source).contains(attemptedMove)) {
            throw new IllegalStateException("Attempted move " + attemptedMove + " is invalid!");
        }
        updateInformationForThisRound(attemptedMove.<ChessBoard<P>>getTransition().transition(chessBoard), false);
    }

    @Override
    public void undoLastRound() {
        if (gameStatus == GameStatus.CHECKMATE || gameStatus == GameStatus.STALEMATE) {
            throw new IllegalStateException("Game has ended in " + gameStatus);
        }
        if (history.getHistorySize() < 2) {
            throw new IllegalStateException("Player has not made move yet!");
        }
        TransitionResult<Square, P> lastOpponentMove = history.popLastResult();
        updateInformationForThisRound(lastOpponentMove.getReverseMove().getTransition().transition(chessBoard), true);
        TransitionResult<Square, P> lastMyMove = history.popLastResult();
        updateInformationForThisRound(lastMyMove.getReverseMove().getTransition().transition(chessBoard), true);
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public RuntimeInformation<Square, P> getRuntimeInformation() {
        return runtimeInformation;
    }

    @Override
    public GameSetting.GridGame<Square, P> getSetting() {
        return gameSetting;
    }
}
