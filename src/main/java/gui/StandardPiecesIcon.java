package gui;

import core.piece.StandardPieces;
import core.player.Player;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

/**
 * Provider of standard pieces icons
 */
public enum StandardPiecesIcon implements PiecesIcon<StandardPieces> {

    VALUE;

    private static final Table<StandardPieces, Player, String> resources;

    static {
        ImmutableTable.Builder<StandardPieces, Player, String> builder = ImmutableTable.builder();
        for (StandardPieces pieceType: StandardPieces.values()) {
            for (Player player: Player.values()) {
                // e.g. KING_WHITE.png
                builder.put(pieceType, player, pieceType.name() + "_" + player.name() + ".png");
            }
        }
        resources = builder.build();
    }

    public String getResource(StandardPieces pieceType, Player player) {
        return resources.get(pieceType, player);
    }
}
