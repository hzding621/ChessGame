package chessgame.board;

import chessgame.piece.StandardPieces;

/**
 * A view-only ChessBoard
 */
public interface ChessBoardViewer extends GridViewer<Square, TwoDimension, StandardPieces>,
        Previewer<Square, StandardPieces, ChessBoardViewer, ChessBoard>  {}
