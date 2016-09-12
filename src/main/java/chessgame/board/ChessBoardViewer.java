package chessgame.board;

import chessgame.piece.PieceClass;

/**
 * A view-only ChessBoard
 */
public interface ChessBoardViewer<P extends PieceClass> extends GridViewer<Square, TwoDimension, P>,
        Previewable<Square, P, ChessBoardViewer<P>, ChessBoard<P>> {}
