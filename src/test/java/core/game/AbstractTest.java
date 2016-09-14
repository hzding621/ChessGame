package core.game;

import core.board.ChessBoard;
import core.board.ChessBoardViewer;
import core.board.GridTileBuilder;
import core.board.Square;
import core.board.TwoDimension;
import core.piece.StandardPieces;
import core.rule.BasicRuleBindings;
import core.rule.Rules;

import java.util.function.Consumer;

/**
 * Contains some shared codes across MoveFinderTest and AttackInformationTest
 */
public class AbstractTest {

    protected ChessBoard<StandardPieces> testBoard;
    protected GridTileBuilder<Square, TwoDimension> tile;
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
        tile = testBoard.getGridTileBuilder();
        runtimeInformation.initializeInformation(rules);
        moveFinder.recompute();
    }

    protected void testBoth(Consumer<Boolean> checker) {
        checker.accept(true);
        checker.accept(false);
    }


}
