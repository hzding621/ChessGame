package chessgame.piece;

import chessgame.board.Cell;
import chessgame.board.ChessBoardViewer;
import chessgame.board.Direction;
import chessgame.board.GridViewer;
import chessgame.board.Square;
import chessgame.board.TwoDimension;
import chessgame.game.RuntimeInformation;
import chessgame.move.CastlingMove;
import chessgame.move.Move;
import chessgame.move.SimpleMove;
import chessgame.player.Player;
import chessgame.rule.AbstractPieceRule;
import chessgame.rule.RequiresRuntimeInformation;
import chessgame.rule.SpecialMovePieceRule;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Optional;
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
    public static class KingRule<C extends Cell, P extends PieceClass, D extends Direction<D>,
            B extends GridViewer<C, D, P>> extends AbstractPieceRule<C, P, B>
            implements RequiresRuntimeInformation<C, P> {

        private final RuntimeInformation<C, P> runtimeInformation;

        public KingRule(RuntimeInformation<C, P> runtimeInformation) {
            this.runtimeInformation = runtimeInformation;
        }

        @Override
        public Collection<C> attacking(B board, C position, Player player) {
            return board.getAllDirections().stream()
                    .map(direction -> board.moveSteps(position, direction, 1))
                    .filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<C> attackBlockingPositions(B board, C sourcePosition,
                                                     C targetPosition,
                                                     Player player) {
            if (!attacking(board, sourcePosition, player).contains(targetPosition)) {
                throw new IllegalArgumentException(sourcePosition + " cannot attack " + targetPosition + " !");
            }

            // To block a pawn attack, can only capture pawn (or move away attacked piece)
            return ImmutableList.of(sourcePosition, targetPosition);
        }

        @Override
        public RuntimeInformation<C, P> getRuntimeInformation() {
            return runtimeInformation;
        }
    }

    public static class KingRuleWithCastling extends KingRule<Square, StandardPieces, TwoDimension, ChessBoardViewer>
            implements SpecialMovePieceRule<Square, StandardPieces, ChessBoardViewer> {

        public KingRuleWithCastling(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
            super(runtimeInformation);
        }

        @Override
        public Collection<Move<Square>> specialMove(ChessBoardViewer board, Player player) {
            return castling(board, player);
        }

        private Optional<CastlingMove<Square>> constructCastlingMove(ChessBoardViewer board,
                                                                     Square kingPosition,
                                                                     Square rookPosition,
                                                                     TwoDimension side,
                                                                     Player player) {
            Square kingNewPosition = board.moveSteps(kingPosition, side, 2).get();
            if (getRuntimeInformation().getAttackInformation().isAttacked(kingNewPosition)) {
                return Optional.empty();
            }
            Square rookNewPosition = board.moveSteps(kingNewPosition, side.reverse(), 1).get();
            return Optional.of(new CastlingMove<>(SimpleMove.of(kingPosition, kingNewPosition,player),
                    SimpleMove.of(rookPosition, rookNewPosition,player)));
        }

        /**
         * From Wikipedia: https://en.wikipedia.org/wiki/Castling
         * Castling may only be done if the king has never moved, the rook involved has never moved,
         * the squares between the king and the rook involved are unoccupied, the king is not in check,
         * and the king does not cross over or end on a square in which it would be in check.
         * Castling is one of the rules of chess and is technically a king move (Hooper & Whyld 1992:71).
         * @return non-empty if a castling is valid, empty otherwise
         */
        private Collection<Move<Square>> castling(ChessBoardViewer board, Player player) {
            Square kingPosition = getRuntimeInformation().getPieceInformation().locateKing(player);
            Piece<StandardPieces> king = board.getPiece(kingPosition).get();
            if (getRuntimeInformation().getPieceInformation().getPieceMoveCount(king) > 0
                    || getRuntimeInformation().getAttackInformation().getCheckers().size() > 0) {
                // If King has moved or if king is under check, cannot move
                return ImmutableList.of();
            }
            Optional<Square> leftBound = board.firstOccupant(kingPosition, TwoDimension.WEST);
            Optional<Square> rightBound = board.firstOccupant(kingPosition, TwoDimension.EAST);

            final ImmutableList.Builder<Move<Square>> builder = ImmutableList.builder();
            board.getPiecesOfTypeForPlayer(StandardPieces.ROOK, player).forEach(rookPosition -> {
                if (leftBound.isPresent() && rookPosition.equals(leftBound.get())) {
                    constructCastlingMove(board, kingPosition, rookPosition, TwoDimension.WEST, player)
                            .ifPresent(builder::add);
                } else if (rightBound.isPresent() && rookPosition.equals(rightBound.get())) {
                    constructCastlingMove(board, kingPosition, rookPosition, TwoDimension.EAST, player)
                            .ifPresent(builder::add);
                }
            });

            /* Note: in the case of castling, the move of any rook will never expose king to opponent's latent checkers
             * however in general all pieces involved in a CompositeMove should be checked it is valid, i.e. its move
             * does not expose king
             */
            return builder.build();
        }
    }
}