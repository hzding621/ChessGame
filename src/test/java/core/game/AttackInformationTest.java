package core.game;

import core.board.Square;
import core.piece.StandardPieces;
import core.player.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains tests for AttackInformation
 */
public class AttackInformationTest extends AbstractTest {

    @Test
    public void testIsAttacked() {

        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(3, 8)
                    .set(StandardPieces.KING, Player.WHITE, "B", "2")
                    .set(StandardPieces.KING, Player.BLACK, "B", "7")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            // White king is attacking all its neighbors
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == j) continue;
                    Assert.assertTrue(runtimeInformation.getAttackInformation().isAttacked(tile.at(i, j)));
                }
            }
        });
    }

    @Test
    public void testCheckers() {

        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.<StandardPieces>builder(3, 8)
                    .set(StandardPieces.KING, Player.WHITE, "B", "1")
                    .set(StandardPieces.KING, Player.BLACK, "B", "8")
                    .set(StandardPieces.QUEEN, Player.WHITE, "B", "3")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            // white queen checking
            Assert.assertEquals(1, runtimeInformation.getAttackInformation().getCheckers().size());
            Assert.assertTrue(runtimeInformation.getAttackInformation().getCheckers().contains(tile.at("B", "3")));
        });
    }
}