package chessgame.game;

import chessgame.board.PieceLocator;
import chessgame.board.SquareCell;
import chessgame.game.GameSetting;
import chessgame.piece.King;
import chessgame.piece.KingPawn;
import chessgame.piece.Pawn;
import chessgame.piece.Piece;
import chessgame.player.Player;

import java.util.*;

/**
 * A 4x4 toy peice set that is used for testing purposes
 */
public final class KingPawnGame implements GameSetting<SquareCell, KingPawn, Piece<KingPawn>> {

    private final SquareCell.Builder builder;
    private final Piece whitePawn = new Pawn<SquareCell, KingPawn>(KingPawn.PAWN, Player.WHITE, 0);
    private final Piece blackPawn = new Pawn<SquareCell, KingPawn>(KingPawn.PAWN, Player.BLACK, 0);
    private final Piece whiteKing = new King<SquareCell, KingPawn>(KingPawn.KING, Player.WHITE, 0);
    private final Piece blackKing = new King<SquareCell, KingPawn>(KingPawn.KING, Player.BLACK, 0);

    public KingPawnGame(SquareCell.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Collection<KingPawn> getSupportedTypes() {
        return Collections.singleton(KingPawn.PAWN);
    }

    @Override
    public Collection<PieceLocator<SquareCell, KingPawn, Piece<KingPawn>>>
    constructPiecesOfTypeAndPlayer(KingPawn type, Player player) {
        return player == Player.WHITE
                ? Arrays.asList(PieceLocator.of(builder.at(1,0), whitePawn),
                PieceLocator.of(builder.at(2,0), whiteKing))
                : Arrays.asList(PieceLocator.of(builder.at(1,3), blackPawn),
                PieceLocator.of(builder.at(2,3), blackKing));
    }

    @Override
    public Map<Player, SquareCell> getKingStartingPositions() {
        Map<Player, SquareCell> map = new HashMap<>();
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
