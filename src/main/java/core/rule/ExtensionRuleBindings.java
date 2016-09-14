package core.rule;

import core.board.ChessBoardViewer;
import core.board.Square;
import core.game.RuntimeInformation;
import core.piece.Bishop;
import core.piece.King;
import core.piece.Knight;
import core.piece.Pawn;
import core.piece.Queen;
import core.piece.Rook;
import core.piece.extension.Assassin;
import core.piece.extension.ExtensionPieces;
import core.piece.extension.Ghost;

/**
 * Extension rule bindings include Ghost and Assassin rule
 */
public class ExtensionRuleBindings extends RuleBindings<Square, ExtensionPieces, ChessBoardViewer<ExtensionPieces>> {


    public ExtensionRuleBindings(RuntimeInformation<Square, ExtensionPieces> runtimeInformation) {
        
        bindRule(ExtensionPieces.PAWN, new Pawn<>(runtimeInformation));
        bindRule(ExtensionPieces.ROOK, new Rook<>());
        bindRule(ExtensionPieces.KNIGHT, new Knight<>());
        bindRule(ExtensionPieces.BISHOP, new Bishop<>());
        bindRule(ExtensionPieces.QUEEN, new Queen<>());
        bindRule(ExtensionPieces.KING, new King<>(runtimeInformation));

        bindRule(ExtensionPieces.ASSASSIN, new Assassin<>()); // YAY!
        bindRule(ExtensionPieces.GHOST, new Ghost<>());
    }
}