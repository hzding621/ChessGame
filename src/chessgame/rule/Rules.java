package chessgame.rule;

import chessgame.board.Board;
import chessgame.board.Cell;
import chessgame.board.ChessBoard;
import chessgame.board.SquareCell;
import chessgame.move.Move;
import chessgame.piece.ChessPieceType;
import chessgame.piece.Piece;
import chessgame.piece.PieceType;
import chessgame.player.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a set of rules associated with an instance of game
 */
public class Rules<C extends Cell, A extends PieceType, P extends Piece<A>, B extends Board<C, A, P>> {

    private final PieceRulesBindings<C, A, P, B> pieceRulesBindings;

    private final CheckRules<C, A, P, B> checkRules;

    public Rules(PieceRulesBindings<C, A, P, B> pieceRulesBindings, CheckRules<C, A, P, B> checkRules) {
        this.pieceRulesBindings = pieceRulesBindings;
        this.checkRules = checkRules;
    }



//    public boolean canMoveTo(B board, C source, C target, Player player) {
//        if (!canMoveByPlayer(board, source, player)) {
//            return false;
//        }
//        P piece = board.getPiece(source).get();
//        return pieceRulesBindings.getRule(piece.getPieceClass())
//                        .getNormalMoves(board, source, player)
//                        .stream().anyMatch(c -> c.equals(target));
//    }
//
    private boolean canMoveByPlayer(B board, C position, Player player) {
        if (!board.getPiece(position).isPresent()) {
            return false;
        }
        P piece = board.getPiece(position).get();
        return  piece.getPlayer().equals(player);
    }
//
    public Collection<C> getPossibleMoves(B board, C source, Player player) {
        if (!canMoveByPlayer(board, source, player)) {
            return Collections.EMPTY_LIST;
        }

        P piece = board.getPiece(source).get();
        return pieceRulesBindings.getRule(piece.getPieceClass())
                .getNormalMoves(board, source, player);
    }

    public CheckRules<C, A, P, B> getCheckRules() {
        return checkRules;
    }

    public boolean moveRemovesCheck(C target, B board, Player player, BoardInformation<C, A, P> boardInformation) {
        return getCheckRules().movesRemovesCheck(target, board, player, boardInformation, pieceRulesBindings);
    }
}
