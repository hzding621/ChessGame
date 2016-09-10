package chessgame.game;

import chessgame.board.Square;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains tests for AttackInformation
 */
public class AttackInformationTest extends AbstractTest {

    @Test
    public void testIsAttacked() {

        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.builder(3, 8)
                    .piece(StandardPieces.KING, Player.WHITE, "B", "2")
                    .piece(StandardPieces.KING, Player.BLACK, "B", "7")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            // White king is attacking all its neighbors
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == j) continue;
                    Assert.assertTrue(runtimeInformation.getAttackInformation().isAttacked(cell.at(i, j)));
                }
            }
        });
    }

    @Test
    public void testCheckers() {

        testBoth(b -> {
            GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.builder(3, 8)
                    .piece(StandardPieces.KING, Player.WHITE, "B", "1")
                    .piece(StandardPieces.KING, Player.BLACK, "B", "8")
                    .piece(StandardPieces.QUEEN, Player.WHITE, "B", "3")
                    .starter(Player.BLACK)
                    .build();
            hydrate(config, b);

            // white queen checking
            Assert.assertEquals(1, runtimeInformation.getAttackInformation().getCheckers().size());
            Assert.assertTrue(runtimeInformation.getAttackInformation().getCheckers().stream().anyMatch(attack -> attack.getAttacker().equals(cell.at("B", "3"))));
        });
    }
}