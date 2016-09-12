package chessgame.view;

import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import java.net.URL;
import java.util.Map;

/**
 * Provider of standard pieces icons
 */
public class StandardPiecesIcon {

    private static final Table<StandardPieces, Player, String> resources;

    private StandardPiecesIcon() {
        // Utils
    }

    static {
        ImmutableTable.Builder<StandardPieces, Player, String> builder = ImmutableTable.builder();
        for (StandardPieces pieceType: StandardPieces.values()) {
            for (Player player: Player.values()) {
                // e.g. KING_WHITE.svg
                builder.put(pieceType, player, pieceType.name() + "_" + player.name() + ".png");
            }
        }
        resources = builder.build();
    }

    public static String getResource(StandardPieces pieceType, Player player) {
        return resources.get(pieceType, player);
    }
}
