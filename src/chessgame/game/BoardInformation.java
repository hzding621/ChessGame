package chessgame.game;

import chessgame.board.BoardViewer;
import chessgame.board.Cell;
import chessgame.piece.Piece;
import chessgame.piece.PieceSet;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.*;

/**
 * Stores information about the board at runtime. Is NOT responsible for computing the information.
 */
public final class BoardInformation<C extends Cell, A extends PieceType, P extends Piece<A>, B extends BoardViewer<C, A, P>> {

    private final PieceInformation<C, A, P> pieceInformation;
    private final DefenderInformation<C, A, P, B> defenderInformation = new DefenderInformation<>();
    private final ActorInformation<C, A, P, B> actorInformation = new ActorInformation<>();
    private final PlayerInformation playerInformation;

    private boolean checkmated;

    public BoardInformation(PieceSet<C, A, P> pieceSet) {
        playerInformation = new PlayerInformation(Player.WHITE, Player.BLACK);
        pieceInformation = new PieceInformation<>(pieceSet.getKingStartingPositions());
        checkmated = false;
    }

    /**
     * @return whether cell is under attack
     */
    public boolean isAttacked(C cell) {
        return defenderInformation.getIsAttacked().contains(cell);
    }

    public boolean isCheckmated() {
        return checkmated;
    }

    public PieceInformation<C, A, P> getPieceInformation() {
        return pieceInformation;
    }

    public C locateKing(Player player) {
        return pieceInformation.locateKing(player);
    }

    public void moveKing(Player player, C cell) {
        pieceInformation.updateKingPosition(player, cell);
    }

    public Player getActor() {
        return playerInformation.getActor();
    }

    public Player getDefender() {
        return playerInformation.getDefender();
    }

    public void endGame() {
        checkmated = true;
    }

    public DefenderInformation<C, A, P, B> getDefenderInformation() {
        return defenderInformation;
    }

    public PlayerInformation getPlayerInformation() {
        return playerInformation;
    }

    public ActorInformation<C, A, P, B> getActorInformation() {
        return actorInformation;
    }

    public Map<C, Set<C>> getAvailableMoves() {
        return actorInformation.getAvailableMoves();
    }

}
