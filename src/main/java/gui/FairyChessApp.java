package gui;

import core.game.ExtensionSetting;
import core.piece.extension.ExtensionPieces;
import core.rule.ExtensionRuleBindings;
import javafx.fxml.Initializable;

/**
 * Chess Game with customized chess pieces: Assassin, and Ghost
 */
public class FairyChessApp extends AbstractChessApp {

    private final ChessModel<ExtensionPieces> model;
    private final ChessController<ExtensionPieces> controller;

    public FairyChessApp() {
        this.model = new ChessModel<>(ExtensionSetting.VALUE, ExtensionRuleBindings::new);
        this.controller = new ChessController<>(model);
    }

    @Override
    protected Initializable getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}