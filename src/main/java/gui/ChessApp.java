package gui;

import com.google.common.base.Supplier;
import core.game.ChessGame;
import core.game.StandardSetting;
import core.piece.StandardPieces;
import core.rule.StandardRuleBindings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main Application for chess
 */
public class ChessApp extends AbstractChessApp {

    private final ChessModel<StandardPieces> model;
    private final ChessController<StandardPieces> controller;

    public ChessApp() {
        this.model = new ChessModel<>(StandardSetting.VALUE, StandardRuleBindings::new);
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