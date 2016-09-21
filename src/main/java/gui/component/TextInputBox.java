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
 * A JavaFX window that requests a certain text field and return it to the caller
 */
public final class TextInputBox {

    public static String display(String title, String message, String fieldName, String promptText) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label direction = new Label();
        direction.setText(message);

        HBox input = new HBox(10);
        Label inputLabel = new Label();
        inputLabel.setText(fieldName + ": ");
        TextField inputField = new TextField();
        if (promptText != null) {
            inputField.setPromptText(promptText);
        }
        input.getChildren().addAll(inputLabel, inputField);

        Button button = new Button();
        button.setText("Confirm");
        button.setOnAction(event -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(direction, input, button);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return inputField.getText();
    }
}
