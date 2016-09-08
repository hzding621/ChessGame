package chessgame.game;

import chessgame.board.PieceLocator;
import chessgame.board.Square;
import chessgame.piece.King;
import chessgame.piece.KingPawn;
import chessgame.piece.Pawn;
import chessgame.piece.Piece;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A 4x4 toy peice set that is used for testing purposes
 */
public final class KingPawnGame implements GameSetting<Square, KingPawn> {

    private final Square.Builder builder;
    private final Piece<KingPawn> whitePawn = new Pawn<>(KingPawn.PAWN, Player.WHITE, 0);
    private final Piece<KingPawn> blackPawn = new Pawn<>(KingPawn.PAWN, Player.BLACK, 0);
    private final Piece<KingPawn> whiteKing = new King<>(KingPawn.KING, Player.WHITE, 0);
    private final Piece<KingPawn> blackKing = new King<>(KingPawn.KING, Player.BLACK, 0);

    public KingPawnGame(Square.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Map<Square, Piece<KingPawn>> constructAllPiecesByStartingPosition() {
        return ImmutableMap.<Square, Piece<KingPawn>>builder()
                .put(builder.at(1, 0), whitePawn)
                .put(builder.at(2, 0), whiteKing)
                .put(builder.at(1, 3), blackPawn)
                .put(builder.at(2, 3), blackKing)
                .build();
    }

    @Override
    public Collection<KingPawn> getSupportedTypes() {
        return Collections.singleton(KingPawn.PAWN);
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
};
