package chessgame.board;

import chessgame.piece.PieceClass;
import chessgame.piece.StandardPieces;

/**
 * A view-only ChessBoard
 */
public interface ChessBoardViewer<P extends PieceClass> extends GridViewer<Square, TwoDimension, P>,
        Previewer<Square, P, ChessBoardViewer<P>, ChessBoard<P>>  {}
