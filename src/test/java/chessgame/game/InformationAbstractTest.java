package chessgame.game;

import chessgame.board.GridCellBuilder;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.piece.StandardPieces;
import chessgame.rule.BasicRuleBindings;
import chessgame.rule.Rules;
import com.google.common.collect.ImmutableList;

/**
 * Contains some shared codes across ActorInformationTest and DefenderInformationTest
 */
public class InformationAbstractTest {

    protected RectangularBoard<StandardPieces> testBoard;
    protected GridCellBuilder<Square, TwoDimension> cell;
    protected Rules<Square, StandardPieces, RectangularBoard<StandardPieces>> rules;
    protected BoardInformation<Square, StandardPieces, RectangularBoard<StandardPieces>> boardInformation;

    protected void hydrate(GameSetting.GridGame<Square, StandardPieces> customizedSet) {
        boardInformation = new BoardInformation<>(customizedSet);
        testBoard = new RectangularBoard<>(customizedSet);
        cell = testBoard.getGridCellFactory();
        rules = new Rules<>(new BasicRuleBindings<>(testBoard, boardInformation));
        boardInformation.updateInformationForThisRound(testBoard, rules, ImmutableList::of, true);
    }

}
