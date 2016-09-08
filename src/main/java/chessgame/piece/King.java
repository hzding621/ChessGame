package chessgame.piece;

import chessgame.board.*;
import chessgame.game.DefenderInformation;
import chessgame.game.PieceInformation;
import chessgame.move.Castling;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import chessgame.rule.RequiresDefenderInformation;
import chessgame.rule.RequiresPieceInformation;
import chessgame.rule.SpecialMovePieceRule;
import com.google.common.collect.ImmutableList;
import utility.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that implements King piece moving logic. Specific rules regarding king checking is handled elsewhere
 */
public final class King<P extends PieceClass> extends AbstractPiece<P> {

    public King(P pieceClass, Player player, int id) {
        super(pieceClass, player, id);
    }

    @Override
    public String toString() {
        return "King{" +
                "player=" + getPlayer() +
                ", id=" + getId() +
                '}';
    }
    public static class KingRule<C extends Cell, P extends PieceClass, D extends Direction,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements RequiresPieceInformation<C, P> {

        private final PieceInformation<C, P> pieceInformation;

        public KingRule(B gridViewer, PieceInformation<C, P> pieceInformation) {
            super(gridViewer);
            this.pieceInformation = pieceInformation;
        }

        @Override
        public Collection<C> attacking(C position, Player player) {
            return boardViewer.getAllDirections().stream()
                    .map(direction -> boardViewer.moveSteps(position, direction, 1))
                    .filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<C> attackBlockingPositions(C sourcePosition,
                                                     C targetPosition,
                                                     Player player) {
            if (!attacking(sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return ImmutableList.of(sourcePosition, targetPosition);
        }

        @Override
        public PieceInformation<C, P> getPieceInformation() {
            return pieceInformation;
        }
    }

    public static class KingRuleWithCastling extends KingRule<Square, StandardPieces, TwoDimension, ChessBoard>
            implements SpecialMovePieceRule<Square>, RequiresDefenderInformation<Square, StandardPieces, ChessBoard> {

        private final DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation;

        public KingRuleWithCastling(ChessBoard gridViewer, PieceInformation<Square, StandardPieces> pieceInformation,
                                    DefenderInformation<Square, StandardPieces, ChessBoard> defenderInformation) {
            super(gridViewer, pieceInformation);
            this.defenderInformation = defenderInformation;
        }

        @Override
        public Collection<Move<Square>> specialMove(Player player) {
            return castling(player);
        }

        private Castling<Square> constructCastlingMove(Square kingPosition,
                                                       Square rookPosition,
                                                       TwoDimension side,
                                                       Player player) {
            Square kingNewPosition = boardViewer.moveSteps(kingPosition, side, 2).get();
            Square rookNewPosition = boardViewer.moveSteps(kingNewPosition, side.reverse(), 1).get();
            return new Castling<>(SimpleMove.of(kingPosition, kingNewPosition,player),
                    SimpleMove.of(rookPosition, rookNewPosition,player));
        }

        /**
         * From Wikipedia: https://en.wikipedia.org/wiki/Castling
         * Castling may only be done if the king has never moved, the rook involved has never moved,
         * the squares between the king and the rook involved are unoccupied, the king is not in check,
         * and the king does not cross over or end on a square in which it would be in check.
         * Castling is one of the rules of chess and is technically a king move (Hooper & Whyld 1992:71).
         * @return non-empty if a castling is valid, empty otherwise
         */
        private Collection<Move<Square>> castling(Player player) {
            Square kingPosition = getPieceInformation().locateKing(player);
            Piece<StandardPieces> king = boardViewer.getPiece(kingPosition).get();
            if (getPieceInformation().getPieceMoveCount(king) > 0 || defenderInformation.getCheckers().size() > 0) {
                // If King has moved or if king is under check, cannot move
                return ImmutableList.of();
            }
            Optional<Square> leftBound = boardViewer.firstOccupant(kingPosition, TwoDimension.WEST);
            Optional<Square> rightBound = boardViewer.firstOccupant(kingPosition, TwoDimension.EAST);

            final ImmutableList.Builder<Move<Square>> builder = ImmutableList.builder();
            boardViewer.getPiecesOfTypeForPlayer(StandardPieces.ROOK, player)
                    .stream()
                    .forEach(rookPosition -> {
                        if (leftBound.isPresent() && rookPosition.equals(leftBound.get())) {
                            builder.add(constructCastlingMove(kingPosition, rookPosition, TwoDimension.WEST, player));
                        } else if (rightBound.isPresent() && rookPosition.equals(rightBound.get())) {
                            builder.add(constructCastlingMove(kingPosition, rookPosition, TwoDimension.EAST, player));
                        }
                    });

            /* Note: in the case of castling, the move of any rook will never expose king to opponent's latent checkers
             * however in general all pieces involved in a CompositeMove should be checked it is valid, i.e. its move
             * does not expose king
             */
            return builder.build();
        }

        @Override
        public DefenderInformation<Square, StandardPieces, ChessBoard> getDefenderInformation() {
            return defenderInformation;
        }
    }
}