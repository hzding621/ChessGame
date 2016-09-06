package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.move.Move;
import chessgame.piece.PieceClass;
import chessgame.player.Player;

import java.util.*;

/**
 * Stores information about the board at runtime. Is NOT responsible for computing the information.
 */
public final class BoardInformation<C extends Cell, P extends PieceClass, B extends BoardViewer<C, P>> {

    private final PieceInformation<C, P> pieceInformation;
    private final DefenderInformation<C, P, B> defenderInformation = new DefenderInformation<>();
    private final ActorInformation<C, P, B> actorInformation = new ActorInformation<>();
    private final PlayerInformation playerInformation;

    public BoardInformation(GameSetting<C, P> gameSetting) {
        playerInformation = new PlayerInformation(gameSetting.starter(), gameSetting.starter().next());
        pieceInformation = new PieceInformation<>(gameSetting.getKingStartingPositions());
    }

    /**
     * @return whether cell is under attack
     */
    public boolean isAttacked(C cell) {
        return defenderInformation.getIsAttacked().contains(cell);
    }

    public PieceInformation<C, P> getPieceInformation() {
        return pieceInformation;
    }

    public C locateKing(Player player) {
        return pieceInformation.locateKing(player);
    }

    public Player getActor() {
        return playerInformation.getActor();
    }

    public Player getDefender() {
        return playerInformation.getDefender();
    }

    public DefenderInformation<C, P, B> getDefenderInformation() {
        return defenderInformation;
    }

    public PlayerInformation getPlayerInformation() {
        return playerInformation;
    }

    public ActorInformation<C, P, B> getActorInformation() {
        return actorInformation;
    }

    public Map<C, Set<Move<C>>> getAvailableMoves() {
        return actorInformation.getAvailableMoves();
    }

}
