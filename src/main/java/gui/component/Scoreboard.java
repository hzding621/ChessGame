package gui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Scoreboard window to show the current score of both players
 */
public final class Scoreboard {

    public static void display(String whiteName, String blackName, double whiteScore, double blackScore) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scoreboard");
        window.setMinWidth(150);

        HBox white = new HBox(10);
        white.getChildren().add(new Label(whiteName));
        white.getChildren().add(new Label(Double.toString(whiteScore)));
        white.setAlignment(Pos.CENTER);

        HBox black = new HBox(10);
        black.getChildren().add(new Label(blackName));
        black.getChildren().add(new Label(Double.toString(blackScore)));
        black.setAlignment(Pos.CENTER);

        Button ok = new Button("OK");
        ok.setOnAction(event -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(white, black, ok);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
