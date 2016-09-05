package chessgame.piece;

import chessgame.board.Coordinate;
import chessgame.board.SquareCell;
import chessgame.player.Player;

import java.util.*;

/**
 * Represents a standard 8x8 chess piece set
 */
public final class ChessPieceSet implements PieceSet<SquareCell, ChessPieceType, Piece<ChessPieceType>> {

    private final Map<Player, Map<ChessPieceType, List<SquareCell>>> configuration = new HashMap<>();
    private final Map<Player, SquareCell> kingStartingPositions = new HashMap<>();

    {
        Coordinate.Factory coordinateFactory = new Coordinate.Factory(8);
        SquareCell.Factory factory = new SquareCell.Factory(coordinateFactory, coordinateFactory);
        configuration.put(Player.WHITE, new HashMap<>());
        configuration.put(Player.BLACK, new HashMap<>());

        // Definition of standard chess piece locations
        populatePieces(Player.WHITE, ChessPieceType.PAWN, factory, new int[][] {
                {0, 1}, {1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}
        });
        populatePieces(Player.BLACK, ChessPieceType.PAWN, factory, new int[][] {
                {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6}
        });
        populatePieces(Player.WHITE, ChessPieceType.ROOK, factory, new int[][] {
                {0, 0}, {7, 0}
        });
        populatePieces(Player.BLACK, ChessPieceType.ROOK, factory, new int[][] {
                {0, 7}, {7, 7}
        });
        populatePieces(Player.WHITE, ChessPieceType.KNIGHT, factory, new int[][] {
                {1, 0}, {6, 0}
        });
        populatePieces(Player.BLACK, ChessPieceType.KNIGHT, factory, new int[][] {
                {1, 7}, {6, 7}
        });
        populatePieces(Player.WHITE, ChessPieceType.BISHOP, factory, new int[][] {
                {2, 0}, {5, 0}
        });
        populatePieces(Player.BLACK, ChessPieceType.BISHOP, factory, new int[][] {
                {2, 7}, {5, 7}
        });
        populatePieces(Player.WHITE, ChessPieceType.QUEEN, factory, new int[][] {
                {3, 0}
        });
        populatePieces(Player.BLACK, ChessPieceType.QUEEN, factory, new int[][] {
                {3, 7}
        });
        populatePieces(Player.WHITE, ChessPieceType.KING, factory, new int[][] {
                {4, 0}
        });
        populatePieces(Player.BLACK, ChessPieceType.KING, factory, new int[][] {
                {4, 7}
        });

        // Define King starting position
        kingStartingPositions.put(Player.WHITE, factory.of(4, 0).get());
        kingStartingPositions.put(Player.BLACK, factory.of(4, 7).get());
    }

    public Map<Player, Map<ChessPieceType, List<SquareCell>>> getConfiguration() {
        return configuration;
    }

    @Override
    public Map<Player, SquareCell> getKingStartingPositions() {
        return kingStartingPositions;
    }

    @Override
    public boolean doesIncludePiece(ChessPieceType type) {

        // By definition the chess piece set include all types of chess pieces
        return true;
    }

    @Override
    public Collection<ChessPieceType> getAllTypes() {
        return configuration.get(Player.WHITE).keySet();
    }

    @Override
    public int getPieceCountForOnePlayer(ChessPieceType type) {
        return configuration.get(Player.WHITE).get(type).size();
    }

    @Override
    public Collection<SquareCell> getStartingPositions(ChessPieceType type, Player player) {
        return configuration.get(player).get(type);
    }

    private void populatePieces(Player player,
                                       ChessPieceType type,
                                       SquareCell.Factory factory, int[][] indices) {
        for (int[] position: indices) {
            configuration.get(player).putIfAbsent(type, new ArrayList<>());
            try {
                configuration.get(player).get(type).add(factory.of(position[0], position[1]).get());
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Invalid configured starting position for "
                                                + type + " at " + position[0] + "," + position[1]);
            }
        }
    }
}
