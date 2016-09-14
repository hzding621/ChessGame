package core.board;

import core.game.StandardSetting;
import core.move.Move;
import core.move.SimpleMove;
import core.piece.StandardPieces;
import core.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests for ChessBoard
 */
public final class ChessBoardTest {

    private ChessBoard<StandardPieces> chessBoard;
    private Square.Builder tile;


    @Before
    public void initializeBoard() {
        this.chessBoard = ChessBoard.create(StandardSetting.VALUE);
        this.tile = new Square.Builder(new Coordinate.Builder(8), new Coordinate.Builder(8));
    }

    @Test
    public void testPreview() {
        Move<Square, StandardPieces> simpleMove = SimpleMove.of(tile.at("E", "2"), tile.at("E", "4"), Player.WHITE);
        Assert.assertTrue(chessBoard.preview(simpleMove.getTransition(), future -> future.isOccupied(tile.at("E", "4"))));

        Assert.assertTrue(chessBoard.isOccupied(tile.at("E", "2")));
    }
}
