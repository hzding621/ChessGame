package chessgame.board;

import chessgame.game.StandardSetting;
import chessgame.piece.StandardPieces;
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
        this.chessBoard = ChessBoard.create(new StandardSetting());
        this.tile = new Square.Builder(new Coordinate.Builder(8), new Coordinate.Builder(8));
    }

    @Test
    public void testPreview() {
        ChessBoardViewer<StandardPieces> preivew = chessBoard.preview(mutableBoard -> {
            mutableBoard.movePiece(tile.at("E", "2"), tile.at("E", "4"));
            return null;
        });
        Assert.assertTrue(preivew.isOccupied(tile.at("E", "4")));
        Assert.assertTrue(chessBoard.isOccupied(tile.at("E", "2")));
    }
}
