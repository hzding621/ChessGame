package gui;

import core.game.StandardSetting;
import core.piece.StandardPieces;
import core.rule.StandardRuleBindings;
import javafx.fxml.Initializable;

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