package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Square;
import chessgame.move.Move;
import chessgame.move.TransitionResult;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Rules;

import java.util.Collection;

/**
 * Main class for implementing main loop in a chess game
 */
public class ChessGame<P extends PieceClass> implements Game<Square, P, ChessBoard<P>> {
    private final ChessBoard<P> chessBoard;
    private final Rules<Square, P, ChessBoardViewer<P>> chessRules;
    private final RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation;
    private final MoveFinder<Square, P> moveFinder;
    private GameStatus gameStatus = GameStatus.OPEN;


    protected ChessGame(ChessBoard<P> chessBoard,
                      Rules<Square, P, ChessBoardViewer<P>> chessRules,
                      RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation,
                      MoveFinder<Square, P> moveFinder) {
        this.chessBoard = chessBoard;
        this.chessRules = chessRules;
        this.runtimeInformation = runtimeInformation;
        this.moveFinder = moveFinder;
        runtimeInformation.initializeInformation(chessRules);
        moveFinder.recompute();
    }

    public static <P extends PieceClass> ChessGame<P> create(GameSetting.GridGame<Square, P> setting,
                                                             ChessRuleBindings.Supplier<P> ruleBindingSupplier) {
        ChessBoard<P> board = ChessBoard.create(setting);
        RuntimeInformationImpl<Square, P, ChessBoardViewer<P>> runtimeInformation =
                new RuntimeInformationImpl<>(setting, board);
        Rules<Square, P, ChessBoardViewer<P>> rules =
                new Rules<>(ruleBindingSupplier.get(runtimeInformation));
        MoveFinder<Square, P> moveFinder = new BasicMoveFinder<>(board, rules, runtimeInformation);
        return new ChessGame<>(board, rules, runtimeInformation, moveFinder);
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
    public Collection<Move<Square>> availableMoves() {
        return moveFinder.getAvailableMoves().values();
    }

    @Override
    public Collection<Move<Square>> availableMovesFrom(Square square) {
        return moveFinder.getAvailableMoves().get(square);
    }

    private void updateInformationForThisRound(TransitionResult<Square, P> history) {
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
