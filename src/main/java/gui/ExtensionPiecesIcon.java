package gui;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import core.piece.extension.ExtensionPieces;
import core.player.Player;

/**
 * Mapping from ExtensionPieces to its resources
 */
public enum ExtensionPiecesIcon implements PiecesIcon<ExtensionPieces> {

    VALUE;

    private static final Table<ExtensionPieces, Player, String> resources;

    static {
        ImmutableTable.Builder<ExtensionPieces, Player, String> builder = ImmutableTable.builder();
        for (ExtensionPieces pieceType: ExtensionPieces.values()) {
            for (Player player: Player.values()) {
                // e.g. KING_WHITE.png
                builder.put(pieceType, player, pieceType.name() + "_" + player.name() + ".png");
            }
        }
        resources = builder.build();
    }

    public String getResource(ExtensionPieces pieceType, Player player) {
        return resources.get(pieceType, player);
    }
}
