package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.move.MoveResult;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.player.Player;
import chessgame.rule.Attack;
import chessgame.rule.LatentAttack;
import chessgame.rule.Rules;
import com.google.common.collect.SetMultimap;

import java.util.Set;

/**
 * A composite class that holds all major runtime information
 */
public final class BoardInformation<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>>
        implements PieceInformation<C, P>, PlayerInformation, ActorInformation<C, P, B>, DefenderInformation<C, P, B> {

    private final PieceInformationImpl<C, P> pieceInformation;
    private final PlayerInformationImpl playerInformation;
    private final DefenderInformationImpl<C, P, B> defenderInformation = new DefenderInformationImpl<>();
    private final ActorInformationImpl<C, P, B> actorInformation = new ActorInformationImpl<>();

    public BoardInformation(GameSetting<C, P> gameSetting) {
        playerInformation = new PlayerInformationImpl(gameSetting.starter(), gameSetting.starter().next());
        pieceInformation = new PieceInformationImpl<>(gameSetting.getKingStartingPositions());
    }

    public GameStatus updateInformationForThisRound(B chessBoard,
                                                    Rules<C, P, B> chessRules,
                                                    MoveResult<C, P> history,
                                                    boolean opening) {
        if (!opening) {
            playerInformation.nextRound();
        }
        pieceInformation.updateInformation(history);

        // Defender information is used to compute actor information so must be computed earlier
        defenderInformation.refresh(chessBoard, chessRules, playerInformation, locateKing(getActor()));
        actorInformation.refresh(chessBoard, chessRules, playerInformation, defenderInformation);

        // Update Checkmate/Stalemate situation
        if (actorInformation.getAvailableMoves().isEmpty()) {
            if (defenderInformation.getCheckers().isEmpty()) {
                 return GameStatus.STALEMATE;
            } else {
                return GameStatus.CHECKMATE;
            }
        }
        return GameStatus.OPEN;
    }

    @Override
    public int getPieceMoveCount(Piece<P> piece) {
        return pieceInformation.getPieceMoveCount(piece);
    }

    @Override
    public C locateKing(Player player) {
        return pieceInformation.locateKing(player);
    }

    @Override
    public Player getActor() {
        return playerInformation.getActor();
    }

    @Override
    public Player getDefender() {
        return playerInformation.getDefender();
    }

    @Override
    public SetMultimap<C, Move<C>> getAvailableMoves() {
        return actorInformation.getAvailableMoves();
    }

    @Override
    public boolean isAttacked(C cell) {
        return defenderInformation.isAttacked(cell);
    }

    @Override
    public Set<Attack<C>> getCheckers() {
        return defenderInformation.getCheckers();
    }

    @Override
    public Set<LatentAttack<C>> getLatentCheckersByBlocker(C blocker) {
        return defenderInformation.getLatentCheckersByBlocker(blocker);
    }
}
