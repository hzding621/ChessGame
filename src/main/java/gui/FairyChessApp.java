package gui;

import core.game.ExtensionSetting;
import core.game.StandardSetting;
import core.piece.StandardPieces;
import core.piece.extension.ExtensionPieces;
import core.rule.ExtensionRuleBindings;
import core.rule.StandardRuleBindings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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