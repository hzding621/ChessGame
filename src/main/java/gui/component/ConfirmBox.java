package gui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ConfirmBox {

    public static boolean display(String title, String message, String yesButtonText, String noButtonText) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label confirmMessage = new Label();
        confirmMessage.setText(message);

        //Create two buttons
        Button yesButton = new Button(yesButtonText);
        Button noButton = new Button(noButtonText);

        BooleanProperty answer = new SimpleBooleanProperty();

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer.setValue(true);
            window.close();
        });
        noButton.setOnAction(e -> {
            answer.setValue(false);
            window.close();
        });
        HBox buttons = new HBox(10);
        //Add buttons
        buttons.getChildren().addAll(confirmMessage, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(confirmMessage, buttons);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer.get();
    }
}