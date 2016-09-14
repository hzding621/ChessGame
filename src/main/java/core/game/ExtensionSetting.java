package core.game;

import core.board.Square;
import core.piece.Piece;
import core.piece.StandardPieces;
import core.piece.extension.ExtensionPieces;
import core.player.Player;
import utility.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * Chess game with the inclusion of Assassin and Ghost
 */
public enum ExtensionSetting implements GameSetting.GridGame<Square, ExtensionPieces> {

    VALUE;

    private static final ConfigurableGameSetting<ExtensionPieces> delegate;

    private static void populatePieces(ConfigurableGameSetting.Builder<ExtensionPieces> builder,
                                       ExtensionPieces type, int x, int y, int fileLength, int rankLength) {
        builder.set(type, Player.WHITE, x, y)
                .set(type, Player.WHITE, fileLength - 1 - x, y)
                .set(type, Player.BLACK, x, rankLength - 1 - y)
                .set(type, Player.BLACK, fileLength - 1 - x, rankLength - 1 - y);
    }

    static {

        ConfigurableGameSetting.Builder<ExtensionPieces> builder = ConfigurableGameSetting.builder(10,8);
        for (int i = 0; i < 5; i++) {
            populatePieces(builder, ExtensionPieces.PAWN, i, 1, 10, 8);
        }
        populatePieces(builder, ExtensionPieces.ROOK, 0, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.KNIGHT, 1, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.BISHOP, 2, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.ASSASSIN, 3, 0, 10, 8); // Assassins are in between Bishops and Queens
        builder.set(ExtensionPieces.QUEEN, Player.WHITE, 4, 0);
        builder.set(ExtensionPieces.QUEEN, Player.BLACK, 4, 7);
        builder.set(ExtensionPieces.KING, Player.WHITE, 5, 0);
        builder.set(ExtensionPieces.KING, Player.BLACK, 5, 7);

        Pair<Integer, Integer> pair = getTwoRandomDistinctNumber(10);
        builder.reset(ExtensionPieces.GHOST, Player.WHITE, pair.first(), 1); // Ghost takes the positions of two random pawns!
        builder.reset(ExtensionPieces.GHOST, Player.WHITE, pair.second(), 1);

        pair = getTwoRandomDistinctNumber(10);
        builder.reset(ExtensionPieces.GHOST, Player.BLACK, pair.first(), 6);
        builder.reset(ExtensionPieces.GHOST, Player.BLACK, pair.second(), 6);
        delegate = builder.build();

    }

    private static Pair<Integer, Integer> getTwoRandomDistinctNumber(int n) {

        Random random = new Random();
        int i = random.nextInt(n-1);
        int j = random.nextInt(n);
        if (j == i) {
            j = n-1;
        }
        return Pair.of(i, j);
    }


    @Override
    public Map<Square, Piece<ExtensionPieces>> constructPiecesByStartingPosition() {
        return delegate.constructPiecesByStartingPosition();
    }

    @Override
    public Map<Player, Square> getKingStartingPositions() {
        return delegate.getKingStartingPositions();
    }

    @Override
    public Collection<Player> getPlayers() {
        return delegate.getPlayers();
    }

    @Override
    public Player getStarter() {
        return delegate.getStarter();
    }

    @Override
    public int getFileLength() {
        return delegate.getFileLength();
    }

    @Override
    public int getRankLength() {
        return delegate.getRankLength();
    }
}
