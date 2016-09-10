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
 * Contains some shared codes across MoveFinderTest and AttackInformationTest
 */
public class InformationAbstractTest {

    protected RectangularBoard.Instance<StandardPieces> testBoard;
    protected GridCellBuilder<Square, TwoDimension> cell;
    protected Rules<Square, StandardPieces, RectangularBoard.Instance<StandardPieces>> rules;
    protected RuntimeInformationImpl<Square, StandardPieces, RectangularBoard.Instance<StandardPieces>> runtimeInformation;
    protected MoveFinder<Square, StandardPieces> moveFinder;

    protected void hydrate(GameSetting.GridGame<Square, StandardPieces> customizedSet) {
        testBoard = RectangularBoard.Instance.create(customizedSet);
        runtimeInformation = new RuntimeInformationImpl<>(customizedSet, testBoard);
        rules = new Rules<>(new BasicRuleBindings<>(runtimeInformation));
        moveFinder = new OptimizedMoveFinder<>(testBoard, rules, runtimeInformation);
        cell = testBoard.getGridCellFactory();
        runtimeInformation.initializeInformation(rules);
        moveFinder.recompute();
    }

}
