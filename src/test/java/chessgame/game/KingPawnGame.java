package chessgame.game;

import chessgame.board.PieceLocator;
import chessgame.board.Square;
import chessgame.piece.King;
import chessgame.piece.KingPawn;
import chessgame.piece.Pawn;
import chessgame.piece.Piece;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.*;

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
    public Collection<KingPawn> getSupportedTypes() {
        return Collections.singleton(KingPawn.PAWN);
    }

    @Override
    public Collection<PieceLocator<Square, KingPawn>>
    constructPiecesOfTypeAndPlayer(KingPawn type, Player player) {
        return player == Player.WHITE
                ? ImmutableList.of(PieceLocator.of(builder.at(1,0), whitePawn),
                PieceLocator.of(builder.at(2,0), whiteKing))
                : ImmutableList.of(PieceLocator.of(builder.at(1,3), blackPawn),
                PieceLocator.of(builder.at(2,3), blackKing));
    }

    @Override
    public Map<Player, Square> getKingStartingPositions() {
        Map<Player, Square> map = new HashMap<>();
        map.put(Player.WHITE, builder.at(2, 0));
        map.put(Player.BLACK, builder.at(2, 3));
        return map;
    }

    @Override
    public Collection<Player> getPlayers() {
        return Arrays.asList(Player.WHITE, Player.BLACK);
    }

    @Override
    public Player starter() {
        return Player.WHITE;
    }
};
