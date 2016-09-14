package core.game;

import core.board.BoardViewer;
import core.board.Tile;
import core.move.TransitionResult;
import core.piece.PieceClass;
import core.rule.Rules;

/**
 * A composite class that holds all major runtime information
 */
public final class RuntimeInformationImpl<C extends Tile, P extends PieceClass, B extends BoardViewer<C, P>> implements RuntimeInformation<C, P> {

    private final PieceInformationImpl<C, P> pieceInformation;
    private final PlayerInformationImpl playerInformation;
    private final AttackInformationImpl<C, P, B> attackInformation;

    public RuntimeInformationImpl(GameSetting<C, P> gameSetting, B board) {
        this.playerInformation = new PlayerInformationImpl(gameSetting.getStarter(), gameSetting.getStarter().next());
        this.pieceInformation = new PieceInformationImpl<>(gameSetting.getKingStartingPositions());
        this.attackInformation = new AttackInformationImpl<>(board, this);
    }

    public void initializeInformation(Rules<C, P, B> rules) {
        attackInformation.update(rules);
    }

    public void updateInformationForThisRound(Rules<C, P, B> rules, TransitionResult<C, P> history) {
        playerInformation.nextRound();
        pieceInformation.update(history);
        attackInformation.update(rules);
    }

    @Override
    public PlayerInformation getPlayerInformation() {
        return playerInformation;
    }

    @Override
    public PieceInformation<C, P> getPieceInformation() {
        return pieceInformation;
    }

    @Override
    public AttackInformation<C> getAttackInformation() {
        return attackInformation;
    }
}
