package core.piece;

import core.player.Player;

/**
 * Represents an abstract chess piece that belongs to a piece type.
 * Pieces should NOT store position information to avoid state duplication.
 * Different concrete piece types should inherit this type.
 */
public interface Piece<P extends PieceClass> {

    P getPieceClass();

    Player getPlayer();

    int getId();
}
