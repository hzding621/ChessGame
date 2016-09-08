package chessgame.game;

import chessgame.board.Coordinate;
import chessgame.board.Square;
import chessgame.piece.Bishop;
import chessgame.piece.King;
import chessgame.piece.Knight;
import chessgame.piece.Pawn;
import chessgame.piece.Piece;
import chessgame.piece.Queen;
import chessgame.piece.Rook;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a standard 8x8 chess piece set
 */
public final class StandardSetting implements GameSetting.GridGame<Square, StandardPieces> {

    private final Table<Player, StandardPieces, List<Square>> configuration = HashBasedTable.create(2, 6);
    private final Map<Player, Square> kingStartingPositions = new HashMap<>();

    {
        Coordinate.Builder coordinateBuilder = new Coordinate.Builder(8);
        Square.Builder builder = new Square.Builder(coordinateBuilder, coordinateBuilder);

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

    @Override
    public Map<Square, Piece<StandardPieces>> constructPiecesByStartingPosition() {

        TreeMap<Square, Piece<StandardPieces>> map = new TreeMap<>();
        configuration.cellSet().forEach(cell -> {
            for (int i = 0; i < cell.getValue().size(); i++) {
                map.put(cell.getValue().get(i), createPiece(cell.getColumnKey(), cell.getRowKey(), i));
            }
        });
        return map;
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
    public Player getStarter() {
        return Player.WHITE;
    }

    private void populatePieces(Player player,
                                StandardPieces type,
                                Square.Builder builder,
                                int[][] indices) {
        for (int[] position: indices) {
            if (!configuration.contains(player, type)) {
                configuration.put(player, type, Lists.newArrayList());
            }
            configuration.get(player, type).add(builder.at(position[0], position[1]));
        }
    }

    @Override
    public int getRankLength() {
        return 8;
    }

    @Override
    public int getFileLength() {
        return 8;
    }
}
