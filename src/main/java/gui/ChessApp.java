package gui;

import core.game.ChessGame;
import core.game.ExtensionSetting;
import core.game.StandardSetting;
import core.piece.StandardPieces;
import core.piece.extension.ExtensionPieces;
import core.rule.ExtensionRuleBindings;
import core.rule.StandardRuleBindings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main Application for chess
 */
public class ChessApp extends Application {

    private final ChessGame<StandardPieces> game;
    private final ChessModel<StandardPieces> model;
    private final ChessController<StandardPieces> controller;

    public ChessApp() {
        this.game = ChessGame.create(StandardSetting.VALUE, StandardRuleBindings::new);
        this.model = new ChessModel<>(game);
        this.controller = new ChessController<>(model);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}