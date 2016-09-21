package gui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Display a Message on top
 */
public final class MessageBox {

    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label direction = new Label();
        direction.setText(message);

        Button button = new Button();
        button.setText("OK");
        button.setOnAction(event -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(direction, button);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
