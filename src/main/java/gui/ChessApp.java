package gui;

import com.google.common.base.Supplier;
import core.game.ChessGame;
import core.game.StandardSetting;
import core.piece.StandardPieces;
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

    private final Supplier<ChessGame<StandardPieces>> gameSupplier = () -> ChessGame.create(StandardSetting.VALUE, StandardRuleBindings::new);
    private final ChessModel<StandardPieces> model;
    private final ChessController<StandardPieces> controller;

    public ChessApp() {
        this.model = new ChessModel<>(gameSupplier);
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