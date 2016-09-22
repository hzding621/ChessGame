package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Chess App that is inherited by (Standard) ChessApp and FairyChessApp
 */
public abstract class AbstractChessApp extends Application {

    protected abstract Initializable getController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
        loader.setController(getController());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Application");
        primaryStage.show();
    }
}