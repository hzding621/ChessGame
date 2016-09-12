package chessgame.game;

import chessgame.board.Coordinate;
import chessgame.board.Square;
import chessgame.piece.Piece;
import chessgame.piece.PieceClass;
import chessgame.piece.PieceImpl;
import chessgame.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a customized board configuration. Used for testing purposes.
 */
public final class ConfigurableGameSetting<P extends PieceClass> implements GameSetting.GridGame<Square, P> {

    private final Set<P> supportedTypes;
    private final Map<Square, Piece<P>> piecesByPosition;
    private final Map<Player, Square> kingStartingPositions;
    private final Player starter;
    private final int fileLength;
    private final int rankLength;

    private ConfigurableGameSetting(Set<P> supportedTypes,
                                    Map<Square, Piece<P>> piecesByPosition,
                                    Map<Player, Square> kingStartingPositions,
                                    Player starter,
                                    int fileLength,
                                    int rankLength) {
        this.supportedTypes = supportedTypes;
        this.piecesByPosition = piecesByPosition;
        this.kingStartingPositions = kingStartingPositions;
        this.starter = starter;
        this.fileLength = fileLength;
        this.rankLength = rankLength;
    }

    @Override
    public Map<Square, Piece<P>> constructPiecesByStartingPosition() {
        return piecesByPosition;
    }

    @Override
    public Map<Player, Square> getKingStartingPositions() {
        return kingStartingPositions;
    }

    @Override
    public Collection<Player> getPlayers() {
        return ImmutableList.of(Player.WHITE, Player.BLACK);
    }

    public static <P extends PieceClass> Builder<P> builder(int fileLength, int rankLength) {
        return new Builder<>(fileLength, rankLength);
    }

    @Override
    public Player getStarter() {
        return starter;
    }

    @Override
    public int getRankLength() {
        return rankLength;
    }

    @Override
    public int getFileLength() {
        return fileLength;
    }

    public static final class Builder<P extends PieceClass> {

        private final int fileLength;
        private final int rankLength;
        private final Square.Builder builder;
        private final Map<Square, Piece<P>> piecesByPosition = new HashMap<>();
        private final Map<P, Integer> pieceTypeCount = new HashMap<>();
        private final Map<Player, Square> kingPositions = new HashMap<>();
        private Player starter = Player.WHITE;

        private Builder(int fileLength, int rankLength) {
            this.builder = new Square.Builder(new Coordinate.Builder(fileLength), new Coordinate.Builder(rankLength));
            this.fileLength = fileLength;
            this.rankLength = rankLength;
        }

        public Builder<P> set(P type, Player player, String file, String rank) {
            Square tile = builder.at(file, rank);
            return set(type, player, tile.getFile().getCoordinate().getIndex(),
                    tile.getRank().getCoordinate().getIndex());
        }

        public Builder<P> set(P type, Player player, int file, int rank) {
            Square position = builder.at(file, rank);
            if (piecesByPosition.containsKey(position)) {
                throw new IllegalStateException("Piece at " + position + " is already set!");
            }
            return reset(type, player, file, rank);
        }

        public Builder<P> reset(P type, Player player, String file, String rank) {
            Square tile = builder.at(file, rank);
            return reset(type, player, tile.getFile().getCoordinate().getIndex(),
                    tile.getRank().getCoordinate().getIndex());
        }

        public Builder<P> reset(P type, Player player, int file, int rank) {
            Square position = builder.at(file, rank);
            if (type.isKing()) {
                if (kingPositions.containsKey(player)) {
                    throw new IllegalStateException("Can only have one king for " + player);
                }
                kingPositions.put(player, builder.at(file, rank));
            }
            pieceTypeCount.put(type, pieceTypeCount.getOrDefault(type, 0) + 1);
            Piece<P> piece = new PieceImpl<>(type, player, pieceTypeCount.get(type));
            piecesByPosition.put(position, piece);
            return this;
        }

        public Builder<P> starter(Player starter) {
            this.starter = starter;
            return this;
        }

        public ConfigurableGameSetting<P> build() {
            if (!kingPositions.containsKey(Player.WHITE) || !kingPositions.containsKey(Player.BLACK)) {
                throw new IllegalStateException("Kings must be set.");
            }
            return new ConfigurableGameSetting<>(pieceTypeCount.keySet(), piecesByPosition, kingPositions, starter,
                    fileLength, rankLength);
        }
    }
}
