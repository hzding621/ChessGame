package chessgame.piece;

import chessgame.board.PieceLocator;
import chessgame.board.SquareCell;
import chessgame.player.Player;

import java.util.*;

/**
 * A 4x4 toy peice set that is used for testing purposes
 */
public final class KingPawnToySet implements PieceSet<SquareCell, KingPawnGame, Piece<KingPawnGame>> {

    private final SquareCell.Builder builder;
    private final Piece whitePawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.WHITE, 0);
    private final Piece blackPawn = new Pawn<SquareCell, KingPawnGame>(KingPawnGame.PAWN, Player.BLACK, 0);
    private final Piece whiteKing = new King<SquareCell, KingPawnGame>(KingPawnGame.KING, Player.WHITE, 0);
    private final Piece blackKing = new King<SquareCell, KingPawnGame>(KingPawnGame.KING, Player.BLACK, 0);

    public KingPawnToySet(SquareCell.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Collection<KingPawnGame> getSupportedTypes() {
        return Collections.singleton(KingPawnGame.PAWN);
    }

    @Override
    public Collection<PieceLocator<SquareCell, KingPawnGame, Piece<KingPawnGame>>>
    constructPiecesOfTypeAndPlayer(KingPawnGame type, Player player) {
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
};
