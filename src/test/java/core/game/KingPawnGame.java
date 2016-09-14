package core.game;

import core.board.Square;
import core.piece.KingPawn;
import core.piece.Piece;
import core.piece.PieceImpl;
import core.player.Player;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Map;

/**
 * A 4x4 toy piece set that is used for testing purposes
 */
public final class KingPawnGame implements GameSetting<Square, KingPawn> {

    private final Square.Builder builder;
    private final Piece<KingPawn> whitePawn = new PieceImpl<>(KingPawn.PAWN, Player.WHITE, 0);
    private final Piece<KingPawn> blackPawn = new PieceImpl<>(KingPawn.PAWN, Player.BLACK, 0);
    private final Piece<KingPawn> whiteKing = new PieceImpl<>(KingPawn.KING, Player.WHITE, 0);
    private final Piece<KingPawn> blackKing = new PieceImpl<>(KingPawn.KING, Player.BLACK, 0);

    public KingPawnGame(Square.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Map<Square, Piece<KingPawn>> constructPiecesByStartingPosition() {
        return ImmutableMap.<Square, Piece<KingPawn>>builder()
                .put(builder.at(1, 0), whitePawn)
                .put(builder.at(2, 0), whiteKing)
                .put(builder.at(1, 3), blackPawn)
                .put(builder.at(2, 3), blackKing)
                .build();
    }

    @Override
    public Map<Player, Square> getKingStartingPositions() {
        ImmutableMap.Builder<Player, Square> map = ImmutableMap.builder();
        map.put(Player.WHITE, builder.at(2, 0)).put(Player.BLACK, builder.at(2, 3));
        return map.build();
    }

    @Override
    public Collection<Player> getPlayers() {
        return ImmutableList.of(Player.WHITE, Player.BLACK);
    }

    @Override
    public Player getStarter() {
        return Player.WHITE;
    }
}
