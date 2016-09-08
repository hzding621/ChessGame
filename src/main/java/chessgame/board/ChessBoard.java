package chessgame.board;

import chessgame.game.StandardSetting;
import chessgame.piece.*;

import java.util.Map;

/**
 * Represents a regular 8x8 chess board
 */
public final class ChessBoard extends RectangularBoard<StandardPieces> {

    public ChessBoard(StandardSetting setting) {
        super(setting);
    }
}
