package gui;

import core.piece.PieceClass;
import core.piece.StandardPieces;
import core.player.Player;

/**
 * An interface that defines a mapping from each type in a PieceClass to its Java image Resource
 */
public interface PiecesIcon<P extends PieceClass> {

    String getResource(P pieceType, Player player);
}
