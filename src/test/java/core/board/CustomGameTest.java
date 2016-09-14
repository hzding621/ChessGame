package core.board;

import core.game.ChessGame;
import core.game.ChessRuleBindings;
import core.game.ConfigurableGameSetting;
import core.game.ExtensionSetting;
import core.game.GameSetting;
import core.move.Move;
import core.piece.extension.Assassin;
import core.piece.Bishop;
import core.piece.extension.ExtensionPieces;
import core.piece.extension.Ghost;
import core.piece.King;
import core.piece.Knight;
import core.piece.Pawn;
import core.piece.Queen;
import core.piece.Rook;
import core.player.Player;
import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utility.Pair;

import java.util.Collection;
import java.util.Random;

/**
 * Testing game with fairy chess pieces and mechanism
 */
public final class CustomGameTest {

    private final Random random = new Random();
    private ConfigurableGameSetting<ExtensionPieces> setting;
    private ChessGame<ExtensionPieces> customGame;
    private ChessRuleBindings.Provider<ExtensionPieces> ruleBindingsProvider;
    private GridTileBuilder<Square, TwoDimension> tileBuilder;

    private static void populatePieces(ConfigurableGameSetting.Builder<ExtensionPieces> builder,
                                       ExtensionPieces type, int x, int y, int fileLength, int rankLength) {
        builder.set(type, Player.WHITE, x, y)
                .set(type, Player.WHITE, fileLength - 1 - x, y)
                .set(type, Player.BLACK, x, rankLength - 1 - y)
                .set(type, Player.BLACK, fileLength - 1 - x, rankLength - 1 - y);
    }
    @Before
    public void initialize() {

        ruleBindingsProvider = runtimeInfo -> {
            ChessRuleBindings<ExtensionPieces> ruleBindings = new ChessRuleBindings<>();
            ruleBindings.bindRule(ExtensionPieces.PAWN, new Pawn<>(runtimeInfo));
            ruleBindings.bindRule(ExtensionPieces.ROOK, new Rook<>());
            ruleBindings.bindRule(ExtensionPieces.KNIGHT, new Knight<>());
            ruleBindings.bindRule(ExtensionPieces.BISHOP, new Bishop<>());
            ruleBindings.bindRule(ExtensionPieces.QUEEN, new Queen<>());
            ruleBindings.bindRule(ExtensionPieces.KING, new King<>(runtimeInfo));

            ruleBindings.bindRule(ExtensionPieces.ASSASSIN, new Assassin<>()); // YAY!
            ruleBindings.bindRule(ExtensionPieces.GHOST, new Ghost<>());

            return ruleBindings;
        };
    }

    public ConfigurableGameSetting<ExtensionPieces> startingPositionNoGhost() {
        ConfigurableGameSetting.Builder<ExtensionPieces> builder = ConfigurableGameSetting.builder(10,8);
        for (int i = 0; i < 5; i++) {
            populatePieces(builder, ExtensionPieces.PAWN, i, 1, 10, 8);
        }
        populatePieces(builder, ExtensionPieces.ROOK, 0, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.KNIGHT, 1, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.BISHOP, 2, 0, 10, 8);
        populatePieces(builder, ExtensionPieces.ASSASSIN, 3, 0, 10, 8); // Assassins are in between Bishops and Queens
        builder.set(ExtensionPieces.QUEEN, Player.WHITE, 4, 0);
        builder.set(ExtensionPieces.QUEEN, Player.BLACK, 4, 7);
        builder.set(ExtensionPieces.KING, Player.WHITE, 5, 0);
        builder.set(ExtensionPieces.KING, Player.BLACK, 5, 7);

        return builder.build();
    }

    private Pair<Integer, Integer> getTwoRandomDistinctNumber(int n) {
        int i = random.nextInt(n-1);
        int j = random.nextInt(n);
        if (j == i) {
            j = n-1;
        }
        return Pair.of(i, j);
    }

    public void hydrate(GameSetting.GridGame<Square, ExtensionPieces> setting) {
        customGame = ChessGame.create(setting, ruleBindingsProvider);
        tileBuilder = customGame.getBoard().getGridTileBuilder();
    }

    @Test
    public void testInitialGhost() {
        hydrate(ExtensionSetting.VALUE);
        Square ghostPosition = Iterables.getFirst(customGame.getBoard().getPieceLocationsOfTypeAndPlayer(ExtensionPieces.GHOST, Player.WHITE), null);
        Collection<Move<Square, ExtensionPieces>> moves = customGame.availableMovesFrom(ghostPosition);
        Assert.assertEquals(40, moves.size()); // Ghost can teleport to all the 10x6 empty spaces in the middle
     }

    @Test
    public void testGhostRemovingCheck() {
        hydrate(ConfigurableGameSetting.<ExtensionPieces>builder(4,4)
                .set(ExtensionPieces.KING, Player.WHITE, 0, 0)
                .set(ExtensionPieces.KING, Player.BLACK, 3, 3)
                .set(ExtensionPieces.ROOK, Player.WHITE, 0, 3)
                .set(ExtensionPieces.GHOST, Player.BLACK, 3, 0)
                .set(ExtensionPieces.PAWN, Player.BLACK, 3, 2)
                .set(ExtensionPieces.PAWN, Player.BLACK, 2, 2)
                .starter(Player.BLACK)
                .build()
        );
        Collection<Move<Square, ExtensionPieces>> moves = customGame.availableMoves();
        Assert.assertEquals(2, moves.size());
    }

    @Test
    public void testInitialAssassinAttack() {
        hydrate(ExtensionSetting.VALUE);
        Collection<Move<Square, ExtensionPieces>> moves = customGame.availableMovesFrom(tileBuilder.at(3, 0));

        int attacks = 2;
        if (customGame.getBoard().getPieceLocationsOfTypeAndPlayer(ExtensionPieces.GHOST, Player.BLACK).contains(tileBuilder.at(3, 6))) {
            attacks--;
        }
        if (customGame.getBoard().getPieceLocationsOfTypeAndPlayer(ExtensionPieces.GHOST, Player.BLACK).contains(tileBuilder.at(9, 6))) {
            attacks--;
        }
        Assert.assertEquals(attacks, moves.size());
    }

    @Test
    public void testInitialAssassinAttackNoGhost() {
        hydrate(startingPositionNoGhost());
        Collection<Move<Square, ExtensionPieces>> moves = customGame.availableMovesFrom(tileBuilder.at(3, 0));
        Assert.assertEquals(2, moves.size());
    }
}
