package chessgame;

import chessgame.board.ChessBoard;
import chessgame.board.SquareCell;
import chessgame.move.Move;
import chessgame.piece.*;
import chessgame.player.Player;
import chessgame.rule.BoardInformation;
import chessgame.rule.CheckRules;
import chessgame.rule.PieceRulesBindings;
import chessgame.rule.Rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the standard chess game
 */
public class ChessGame implements Game<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> {
    private final ChessBoard chessBoard;
    private final Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> chessRules;
    private final Collection<Player> players;
    private final BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> boardInformation;

    private ChessGame(ChessBoard chessBoard,
                      Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> chessRules,
                      Collection<Player> players,
                      BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> boardInformation) {
        this.chessBoard = chessBoard;
        this.chessRules = chessRules;
        this.players = players;
        this.boardInformation = boardInformation;
    }

    public static ChessGame constructGame() {
        PieceSet<SquareCell, ChessPieceType, Piece<ChessPieceType>> pieceSet = new ChessPieceSet();
        ChessBoard chessBoard = new ChessBoard(pieceSet);
        chessBoard.initializeBoard();

        PieceRulesBindings<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> pieceRulesBindings = new PieceRulesBindings<>();
        pieceRulesBindings.addRule(ChessPieceType.BISHOP, new Bishop.BishopRule<>());
        pieceRulesBindings.addRule(ChessPieceType.ROOK, new Rook.RookRule<>());
        pieceRulesBindings.addRule(ChessPieceType.QUEEN, new Queen.QueenRule<>());

        List<Player> players = Arrays.asList(Player.values());
        return new ChessGame(chessBoard, new Rules<>(pieceRulesBindings, new CheckRules<>()), players, new BoardInformation<>());
    }

    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public BoardInformation<SquareCell, ChessPieceType, Piece<ChessPieceType>> getBoardInformation() {
        return boardInformation;
    }

    @Override
    public Rules<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> getRule() {
        return chessRules;
    }

    @Override
    public Collection<Player> getPlayers() {
        return null;
    }

    @Override
    public Collection<SquareCell> allMoves(Player player) {
        return chessBoard.getAllPiecesForPlayer(player).parallelStream()

                // First, source piece cannot be a protector in any king-pinning situation
                .filter(pl -> !(boardInformation.getPinnersForProtector(pl.getCell())
                        .parallelStream()
                        .anyMatch(pinning -> true))
                )
                .map(pl ->
                    chessRules.getPossibleMoves(chessBoard, pl.getCell(), player)
                            .parallelStream()

                            // Second, either the piece to move is not king, or it must not move to an attacked position
                            // Third, each move must be able to remove any check
                            .filter(target ->
                                pl.getPiece().getPieceClass().isKing() && boardInformation.getAttackers(target).isEmpty()
                                || chessRules.moveRemovesCheck(target, chessBoard, player, boardInformation)
                            ))
                .flatMap(e -> e)
                .collect(Collectors.toList());
    }

    @Override
    public void makeMove(Move<SquareCell, ChessPieceType, Piece<ChessPieceType>, ChessBoard> move) {

    }
}
