package chessgame.game;

import chessgame.board.Coordinate;
import chessgame.board.PieceLocator;
import chessgame.board.Square;
import chessgame.piece.ChessPieceType;
import chessgame.piece.Piece;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a customized board configuration. Used for testing purposes.
 */
public final class ConfigurableGameSetting implements GameSetting<Square, ChessPieceType> {

    private final Set<ChessPieceType> supportedTypes;
    private final Map<Player, Map<ChessPieceType, Collection<PieceLocator<Square, ChessPieceType
            >>>> locators;
    private final Map<Player, Square> kingStartingPositions;

    private ConfigurableGameSetting(Set<ChessPieceType> supportedTypes,
                                    Map<Player, Map<ChessPieceType, Collection<PieceLocator<Square, ChessPieceType
                                            >>>> locators,
                                    Map<Player, Square> kingStartingPositions) {
        this.supportedTypes = supportedTypes;
        this.locators = locators;
        this.kingStartingPositions = kingStartingPositions;
    }

    @Override
    public Collection<ChessPieceType> getSupportedTypes() {
        return supportedTypes;
    }

    @Override
    public Collection<PieceLocator<Square, ChessPieceType>>
    constructPiecesOfTypeAndPlayer(ChessPieceType type, Player player) {
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

    @Override
    public Player starter() {
        return Player.WHITE;
    }

    public static final class Builder {

        private final Square.Builder builder;
        private final Map<Player, Map<ChessPieceType, Collection<PieceLocator<Square,
                ChessPieceType>>>> locators = new HashMap<>();
        private final Map<ChessPieceType, Integer> pieceTypeCount = new HashMap<>();
        private final Map<Player, Square> kingPositions = new HashMap<>();

        public Builder(int fileLength, int rankLength) {
            this.builder = new Square.Builder(new Coordinate.Builder(fileLength), new Coordinate.Builder(rankLength));
        }

        public Builder piece(ChessPieceType type, Player player, String file, String rank) {
            Square cell = builder.at(file, rank);
            return piece(type, player, cell.getFile().getCoordinate().getIndex(), cell.getRank().getCoordinate().getIndex());
        }
        public Builder piece(ChessPieceType type, Player player, int file, int rank) {
            if (type == ChessPieceType.KING) {
                if (kingPositions.containsKey(player)) {
                    throw new IllegalStateException("Can only have one king for " + player);
                }
                kingPositions.put(player, builder.at(file, rank));
            }
            pieceTypeCount.put(type, pieceTypeCount.getOrDefault(type, 0) + 1);
            Piece<ChessPieceType> piece = StandardSetting.createPiece(type, player, pieceTypeCount.get(type));
            locators.putIfAbsent(player, new HashMap<>());
            locators.get(player).putIfAbsent(type, new HashSet<>());
            locators.get(player).get(type).add(PieceLocator.of(builder.at(file, rank), piece));
            return this;
        }

        public ConfigurableGameSetting build() {
            if (!kingPositions.containsKey(Player.WHITE) || !kingPositions.containsKey(Player.BLACK)) {
                throw new IllegalStateException("Kings must be set.");
            }
            return new ConfigurableGameSetting(pieceTypeCount.keySet(), locators, kingPositions);
        }
    }
}
