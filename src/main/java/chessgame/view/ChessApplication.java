package chessgame.view;

import chessgame.game.StandardGame;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ChessApplication extends Application {

    private final StandardGame game;
    private final BoardView boardView;

    public ChessApplication() {
        game = StandardGame.create();
        boardView = new BoardView(game);
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(boardView.getGridPane(), 700, 700);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Application");
        primaryStage.show();
    }

    public static void main(String[] args) {

        SvgImageLoaderFactory.install();
        launch(args);
    }
}