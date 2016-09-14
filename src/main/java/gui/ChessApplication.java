package gui;

import core.game.ChessGame;
import core.game.ExtensionSetting;
import core.piece.extension.ExtensionPieces;
import core.rule.ExtensionRuleBindings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChessApplication extends Application {

    private final ChessGame<ExtensionPieces> game;
    private final ChessBoardLayout view;

    public ChessApplication() {
        this.game = ChessGame.create(ExtensionSetting.VALUE, ExtensionRuleBindings::new);
        this.view = new ChessBoardLayout<>(game, ExtensionPiecesIcon.VALUE);
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(view.getLayout());
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}