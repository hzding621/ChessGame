package chessgame.game;

import chessgame.board.Coordinate;
import chessgame.board.PieceLocator;
import chessgame.board.Square;
import chessgame.piece.*;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a standard 8x8 chess piece set
 */
public final class StandardSetting implements GameSetting<Square, StandardPieces> {

    private final Map<Player, Map<StandardPieces, List<Square>>> configuration = new HashMap<>();
    private final Map<Player, Square> kingStartingPositions = new HashMap<>();

    {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        Square.Builder builder = new Square.Builder(coordinateBuilder, coordinateBuilder);
        configuration.put(Player.WHITE, new HashMap<>());
        configuration.put(Player.BLACK, new HashMap<>());

        // Definition of standard chess piece locations
        populatePieces(Player.WHITE, StandardPieces.PAWN, builder, new int[][] {
                {0, 1}, {1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}
        });
        populatePieces(Player.BLACK, StandardPieces.PAWN, builder, new int[][] {
                {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6}
        });
        populatePieces(Player.WHITE, StandardPieces.ROOK, builder, new int[][] {
                {0, 0}, {7, 0}
        });
        populatePieces(Player.BLACK, StandardPieces.ROOK, builder, new int[][] {
                {0, 7}, {7, 7}
        });
        populatePieces(Player.WHITE, StandardPieces.KNIGHT, builder, new int[][] {
                {1, 0}, {6, 0}
        });
        populatePieces(Player.BLACK, StandardPieces.KNIGHT, builder, new int[][] {
                {1, 7}, {6, 7}
        });
        populatePieces(Player.WHITE, StandardPieces.BISHOP, builder, new int[][] {
                {2, 0}, {5, 0}
        });
        populatePieces(Player.BLACK, StandardPieces.BISHOP, builder, new int[][] {
                {2, 7}, {5, 7}
        });
        populatePieces(Player.WHITE, StandardPieces.QUEEN, builder, new int[][] {
                {3, 0}
        });
        populatePieces(Player.BLACK, StandardPieces.QUEEN, builder, new int[][] {
                {3, 7}
        });
        populatePieces(Player.WHITE, StandardPieces.KING, builder, new int[][] {
                {4, 0}
        });
        populatePieces(Player.BLACK, StandardPieces.KING, builder, new int[][] {
                {4, 7}
        });

        // Define King starting position
        kingStartingPositions.put(Player.WHITE, builder.at(4, 0));
        kingStartingPositions.put(Player.BLACK, builder.at(4, 7));
    }

    public Map<Player, Map<StandardPieces, List<Square>>> getConfiguration() {
        return configuration;
    }

    @Override
    public Collection<StandardPieces> getSupportedTypes() {
        return configuration.get(Player.WHITE).keySet();
    }

    @Override
    public Collection<PieceLocator<Square, StandardPieces>> constructPiecesOfTypeAndPlayer(StandardPieces type, Player player) {
        List<Square> startingPositions = configuration.get(player).get(type);
        List<PieceLocator<Square, StandardPieces>> output = new ArrayList<>();
        for (int i = 0; i < startingPositions.size(); i++) {
            output.add(PieceLocator.of(startingPositions.get(i), createPiece(type, player, i)));
        }
        return output;
    }

    public static Piece<StandardPieces> createPiece(StandardPieces type, Player player, int id) {
        switch (type) {
            case PAWN:
                return new Pawn<>(type, player, id);
            case KNIGHT:
                return new Knight<>(type, player, id);
            case BISHOP:
                return new Bishop<>(type, player, id);
            case ROOK:
                return new Rook<>(type, player, id);
            case QUEEN:
                return new Queen<>(type, player, id);
            case KING:
                return new King<>(type, player, id);
        }
        throw new IllegalStateException("Reach unexpected value " + type);
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

    private void populatePieces(Player player,
                                StandardPieces type,
                                Square.Builder builder, int[][] indices) {
        for (int[] position: indices) {
            configuration.get(player).putIfAbsent(type, new ArrayList<>());
            try {
                configuration.get(player).get(type).add(builder.at(position[0], position[1]));
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Invalid configured starting position for "
                                                + type + " at " + position[0] + "," + position[1]);
            }
        }
    }
}
