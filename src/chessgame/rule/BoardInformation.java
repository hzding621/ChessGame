package chessgame.rule;

import chessgame.board.Cell;
import chessgame.board.PieceLocator;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.*;

/**
 * Stores information about the board at runtime. Is NOT responsible for computing the information.
 */
public final class BoardInformation<C extends Cell, A extends PieceType, P extends Piece<A>> {

    private final Map<C, Collection<C>> attacking = new HashMap<>();
    private final Map<C, Collection<KingPinning<C>>> kingPinners = new HashMap<>();
    private final Collection<PieceLocator<C, A, P>> checkers = new HashSet<>();
    private final Map<Player, C> kingPosition = new HashMap();

    /**
     * @return all cells that are attacking `attacked`
     */
    public Collection<C> getAttackers(C attacked) {
        return attacking.getOrDefault(attacked, Collections.EMPTY_LIST);
    }

    /**
     * @return all pinning situation wherein the input piece is the protecting piece
     */
    public Collection<KingPinning<C>> getPinnersForProtector(C protector) {
        return kingPinners.getOrDefault(protector, Collections.EMPTY_LIST);
    }

//    /**
//     * @return all pinning situtation wherein the input piece is the protecting piece
//     */
//    public Collection<PinnedPieces<C>> asProtectedPiece(C protectedPiece) {
//        return asProtected.getOrDefault(protectedPiece, Collections.EMPTY_LIST);
//    }

    public Collection<PieceLocator<C, A, P>> getCheckers() {
        return checkers;
    }

    /**
     * @return the player that is currently under check, if any
     */
    public Optional<Player> isUnderCheck() {
        return Optional.empty();
    }

    /**
     * @return the player that is checkmated, if any
     */
    public Optional<Player> checkmated() {
        return Optional.empty();
    }

    public C getKingPosition(Player player) {
        return kingPosition.get(player);
    }
}
