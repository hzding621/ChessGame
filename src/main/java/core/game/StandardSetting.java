package core.game;

import core.board.Square;
import core.piece.Piece;
import core.piece.StandardPieces;
import core.player.Player;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a standard 8x8 chess piece set
 */
public enum StandardSetting implements GameSetting.GridGame<Square, StandardPieces> {

    VALUE;

    private static final ConfigurableGameSetting<StandardPieces> delegate;

    private static void populateSymmetricPieces(ConfigurableGameSetting.Builder<StandardPieces> builder,
                                                StandardPieces type, int x, int y) {
        builder.set(type, Player.WHITE, x, y).set(type, Player.WHITE, 7 - x, y)
                .set(type, Player.BLACK, x, 7 - y).set(type, Player.BLACK, 7 - x, 7 - y);
    }

    static {

        ConfigurableGameSetting.Builder<StandardPieces> builder = ConfigurableGameSetting.builder(8, 8);
        for (int i = 0; i < 4; i++) {
            populateSymmetricPieces(builder, StandardPieces.PAWN, i, 1);
        }
        populateSymmetricPieces(builder, StandardPieces.ROOK, 0, 0);
        populateSymmetricPieces(builder, StandardPieces.KNIGHT, 1, 0);
        populateSymmetricPieces(builder, StandardPieces.BISHOP, 2, 0);
        builder.set(StandardPieces.QUEEN, Player.WHITE, 3, 0);
        builder.set(StandardPieces.QUEEN, Player.BLACK, 3, 7);
        builder.set(StandardPieces.KING, Player.WHITE, 4, 0);
        builder.set(StandardPieces.KING, Player.BLACK, 4, 7);
        delegate = builder.build();

    }

    @Override
    public Map<Square, Piece<StandardPieces>> constructPiecesByStartingPosition() {
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
