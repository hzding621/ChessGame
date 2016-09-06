package chessgame.game;

import chessgame.board.Coordinate;
import chessgame.board.PieceLocator;
import chessgame.board.Square;
import chessgame.piece.StandardPieces;
import chessgame.piece.Piece;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a customized board configuration. Used for testing purposes.
 */
public final class ConfigurableGameSetting implements GameSetting<Square, StandardPieces> {

    private final Set<StandardPieces> supportedTypes;
    private final Map<Player, Map<StandardPieces, Collection<PieceLocator<Square, StandardPieces>>>> locators;
    private final Map<Player, Square> kingStartingPositions;
    private final Player starter;

    private ConfigurableGameSetting(Set<StandardPieces> supportedTypes,
                                    Map<Player, Map<StandardPieces, Collection<PieceLocator<Square, StandardPieces
                                            >>>> locators,
                                    Map<Player, Square> kingStartingPositions,
                                    Player starter) {
        this.supportedTypes = supportedTypes;
        this.locators = locators;
        this.kingStartingPositions = kingStartingPositions;
        this.starter = starter;
    }

    @Override
    public Collection<StandardPieces> getSupportedTypes() {
        return supportedTypes;
    }

    @Override
    public Collection<PieceLocator<Square, StandardPieces>>
    constructPiecesOfTypeAndPlayer(StandardPieces type, Player player) {
        return locators.getOrDefault(player, Collections.emptyMap()).getOrDefault(type, Collections.emptySet());
    }

    @Override
    public Map<Player, Square> getKingStartingPositions() {
        return kingStartingPositions;
    }

    @Override
    public Collection<Player> getPlayers() {
        return Arrays.asList(Player.WHITE, Player.BLACK);
    }

    public static Builder builder(int fileLength, int rankLength) {
        return new Builder(fileLength, rankLength);
    }

    @Override
    public Player starter() {
        return starter;
    }

    public static final class Builder {

        private final Square.Builder builder;
        private final Map<Player, Map<StandardPieces, Collection<PieceLocator<Square,
                StandardPieces>>>> locators = new HashMap<>();
        private final Map<StandardPieces, Integer> pieceTypeCount = new HashMap<>();
        private final Map<Player, Square> kingPositions = new HashMap<>();
        private Player starter = Player.WHITE;

        private Builder(int fileLength, int rankLength) {
            this.builder = new Square.Builder(new Coordinate.Builder(fileLength), new Coordinate.Builder(rankLength));
        }

        public Builder piece(StandardPieces type, Player player, String file, String rank) {
            Square cell = builder.at(file, rank);
            return piece(type, player, cell.getFile().getCoordinate().getIndex(), cell.getRank().getCoordinate().getIndex());
        }

        public Builder piece(StandardPieces type, Player player, int file, int rank) {
            if (type == StandardPieces.KING) {
                if (kingPositions.containsKey(player)) {
                    throw new IllegalStateException("Can only have one king for " + player);
                }
                kingPositions.put(player, builder.at(file, rank));
            }
            pieceTypeCount.put(type, pieceTypeCount.getOrDefault(type, 0) + 1);
            Piece<StandardPieces> piece = StandardSetting.createPiece(type, player, pieceTypeCount.get(type));
            locators.putIfAbsent(player, new HashMap<>());
            locators.get(player).putIfAbsent(type, new HashSet<>());
            locators.get(player).get(type).add(PieceLocator.of(builder.at(file, rank), piece));
            return this;
        }

        public Builder starter(Player starter) {
            this.starter = starter;
            return this;
        }

        public ConfigurableGameSetting build() {
            if (!kingPositions.containsKey(Player.WHITE) || !kingPositions.containsKey(Player.BLACK)) {
                throw new IllegalStateException("Kings must be set.");
            }
            return new ConfigurableGameSetting(pieceTypeCount.keySet(), locators, kingPositions, starter);
        }
    }
}
