package chessgame.game;

import chessgame.board.ChessBoard;
import chessgame.board.ChessBoardViewer;
import chessgame.board.GridCellBuilder;
import chessgame.board.RectangularBoard;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.piece.StandardPieces;
import chessgame.rule.BasicRuleBindings;
import chessgame.rule.Rules;

import java.util.function.Consumer;

/**
 * Contains some shared codes across MoveFinderTest and AttackInformationTest
 */
public class AbstractTest {

    protected ChessBoard<StandardPieces> testBoard;
    protected GridCellBuilder<Square, TwoDimension> cell;
    protected Rules<Square, StandardPieces, ChessBoardViewer<StandardPieces>> rules;
    protected RuntimeInformationImpl<Square, StandardPieces, ChessBoardViewer<StandardPieces>> runtimeInformation;
    protected MoveFinder<Square, StandardPieces> moveFinder;

    protected void hydrate(GameSetting.GridGame<Square, StandardPieces> customizedSet, boolean optimizedMoveFinder) {
        testBoard = ChessBoard.create(customizedSet);
        runtimeInformation = new RuntimeInformationImpl<>(customizedSet, testBoard);
        rules = new Rules<>(new BasicRuleBindings<>(runtimeInformation));
        if (optimizedMoveFinder) {
            moveFinder = new OptimizedMoveFinder<>(testBoard, rules, runtimeInformation);
        } else {
            moveFinder = new BasicMoveFinder<>(testBoard, rules, runtimeInformation);
        }
        cell = testBoard.getGridCellFactory();
        runtimeInformation.initializeInformation(rules);
        moveFinder.recompute();
    }

    protected void testBoth(Consumer<Boolean> checker) {
        checker.accept(true);
        checker.accept(false);
    }


}
