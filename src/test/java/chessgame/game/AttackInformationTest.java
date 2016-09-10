package chessgame.game;

import chessgame.board.Square;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains tests for AttackInformation
 */
public class AttackInformationTest extends InformationAbstractTest {

    @Test
    public void testIsAttacked() {

        GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.builder(3, 8)
                .piece(StandardPieces.KING, Player.WHITE, "B", "2")
                .piece(StandardPieces.KING, Player.BLACK, "B", "7")
                .starter(Player.BLACK)
                .build();
        hydrate(config);

        // White king is attacking all its neighbors
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) continue;
                Assert.assertTrue(runtimeInformation.getAttackInformation().isAttacked(cell.at(i, j)));
            }
        }
    }

    @Test
    public void testCheckers() {

        GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.builder(3, 8)
                .piece(StandardPieces.KING, Player.WHITE, "B", "1")
                .piece(StandardPieces.KING, Player.BLACK, "B", "8")
                .piece(StandardPieces.QUEEN, Player.WHITE, "B", "3")
                .starter(Player.BLACK)
                .build();
        hydrate(config);

        // white queen checking
        Assert.assertEquals(1, runtimeInformation.getAttackInformation().getCheckers().size());
        Assert.assertTrue(runtimeInformation.getAttackInformation().getCheckers().stream().anyMatch(attack -> attack.getAttacker().equals(cell.at("B", "3"))));
    }

//    @Test
//    public void testLatentCheckers() {
//
//        GameSetting.GridGame<Square, StandardPieces> config = ConfigurableGameSetting.builder(3, 8)
//                .piece(StandardPieces.KING, Player.WHITE, "B", "1")
//                .piece(StandardPieces.KING, Player.BLACK, "B", "8")
//                .piece(StandardPieces.BISHOP, Player.BLACK, "B", "6")
//                .piece(StandardPieces.QUEEN, Player.WHITE, "B", "3")
//                .starter(Player.BLACK)
//                .build();
//        hydrate(config);
//
//        // white queen is pinning black bishop at B6
//        Assert.assertEquals(0, runtimeInformation.getAttackInformation().getCheckers().size());
//        Assert.assertEquals(1, .getLatentCheckersByBlocker(cell.at("B", "6")).size());
//    }
}
