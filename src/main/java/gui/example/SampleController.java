package gui.example;

import gui.ColorScheme;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable {

    @FXML
    GridPane grid;

    public SampleController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}