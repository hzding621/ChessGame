package core.board;

import core.piece.PieceClass;

/**
 * A view-only ChessBoard
 */
public interface ChessBoardViewer<P extends PieceClass> extends GridViewer<Square, TwoDimension, P>,
        Previewable<Square, P, ChessBoardViewer<P>, ChessBoard<P>> {}
