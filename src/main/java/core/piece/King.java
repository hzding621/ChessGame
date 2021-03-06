package core.piece;

import core.board.Tile;
import core.board.ChessBoardViewer;
import core.board.Direction;
import core.board.GridViewer;
import core.board.Square;
import core.board.TwoDimension;
import core.board.StepSize;
import core.game.RuntimeInformation;
import core.move.CastlingMove;
import core.move.Move;
import core.move.SimpleMove;
import core.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that implements King piece moving logic. Specific rules regarding king checking is handled elsewhere
 */
public final class King<C extends Tile, P extends PieceClass, D extends Direction<D>, B extends GridViewer<C, D, P>>
        implements OptimizedPiece<C, P, B>, RequiresRuntimeInformation<C, P>, PieceRule<C,P,B> {

    private final RuntimeInformation<C, P> runtimeInformation;

    public King(RuntimeInformation<C, P> runtimeInformation) {
        this.runtimeInformation = runtimeInformation;
    }

    @Override
    public Collection<C> attacking(B board, C position, Player player) {
        return board.getEveryDirections().stream()
                .map(direction -> board.travelSteps(position, direction, 1, StepSize.of(1, 0)))
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

        // King's attack can't be blocked!
        return ImmutableList.of(sourcePosition);
    }

    @Override
    public RuntimeInformation<C, P> getRuntimeInformation() {
        return runtimeInformation;
    }


    public static final class WithCastling implements OptimizedPiece<Square, StandardPieces, ChessBoardViewer<StandardPieces>>,
            RequiresRuntimeInformation<Square, StandardPieces>, PieceRule<Square, StandardPieces, ChessBoardViewer<StandardPieces>>,
            SpecialMovePiece<Square, StandardPieces, ChessBoardViewer<StandardPieces>> {

        private final King<Square, StandardPieces, TwoDimension, ChessBoardViewer<StandardPieces>> delegate;
        private final RuntimeInformation<Square, StandardPieces> runtimeInformation;

        public WithCastling(RuntimeInformation<Square, StandardPieces> runtimeInformation) {
            this.delegate = new King<>(runtimeInformation);
            this.runtimeInformation = runtimeInformation;
        }

        @Override
        public Collection<Move<Square, StandardPieces>> specialMove(ChessBoardViewer<StandardPieces> board, Player player) {
            return castling(board, player);
        }

        private Optional<CastlingMove<Square, StandardPieces>> constructCastlingMove(ChessBoardViewer<StandardPieces> board,
                                                                     Square kingPosition,
                                                                     Square rookPosition,
                                                                     TwoDimension side,
                                                                     Player player) {
            Square kingNewPosition = board.travelSteps(kingPosition, side, 2, StepSize.of(1, 0)).get();
            if (getRuntimeInformation().getAttackInformation().isAttacked(kingNewPosition)) {
                return Optional.empty();
            }
            Square rookNewPosition = board.travelSteps(kingNewPosition, side.reverse(), 1, StepSize.of(1, 0)).get();
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
        private Collection<Move<Square, StandardPieces>> castling(ChessBoardViewer<StandardPieces> board, Player player) {
            Square kingPosition = getRuntimeInformation().getPieceInformation().locateKing(player);
            Piece<StandardPieces> king = board.getPiece(kingPosition).get();
            if (getRuntimeInformation().getPieceInformation().getPieceMoveCount(king) > 0
                    || getRuntimeInformation().getAttackInformation().getCheckers().size() > 0) {

                // If King has moved or if king is under check, cannot move
                return ImmutableList.of();
            }
            Optional<Square> leftBound = board.firstEncounter(kingPosition, TwoDimension.WEST, StepSize.of(1,0));
            Optional<Square> rightBound = board.firstEncounter(kingPosition, TwoDimension.EAST, StepSize.of(1,0));

            final ImmutableList.Builder<Move<Square, StandardPieces>> builder = ImmutableList.builder();
            board.getPieceLocationsOfTypeAndPlayer(StandardPieces.ROOK, player).forEach(rookPosition -> {
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

        @Override
        public RuntimeInformation<Square, StandardPieces> getRuntimeInformation() {
            return runtimeInformation;
        }

        @Override
        public Collection<Square> attacking(ChessBoardViewer<StandardPieces> board, Square position, Player player) {
            return delegate.attacking(board, position, player);
        }

        @Override
        public Collection<Square> attackBlockingPositions(ChessBoardViewer<StandardPieces> board, Square sourcePosition, Square targetPosition, Player player) {
            /* Note: attackingBlockingPosition is used by OptimizedMoveFinder to find check-escapers,
             * Since any king should never directly attack an opponent king, this method does not matter too much for King piece
             * Therefore it does not matter too much to castling as well
             */
            return delegate.attackBlockingPositions(board, sourcePosition, targetPosition, player);
        }
    }
}