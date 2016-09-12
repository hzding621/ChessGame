package chessgame.board;

import chessgame.game.StandardSetting;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.piece.StandardPieces;
import chessgame.player.Player;
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
        Assert.assertTrue(chessBoard.preview(simpleMove.getTransition(), future -> {
           return future.isOccupied(tile.at("E", "4"));
        }));

        Assert.assertTrue(chessBoard.isOccupied(tile.at("E", "2")));
    }
}
