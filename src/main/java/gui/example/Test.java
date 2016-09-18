package gui.example;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haozhending on 9/18/16.
 */
public class Test {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        ObservableMap<String, String> observableMap = FXCollections.observableMap(map);
        observableMap.addListener((MapChangeListener<String, String>) (change -> {
            if (change.wasAdded()) {
                System.out.println("map[" + change.getKey() + "]=" + change.getValueAdded());
            } else {
                System.out.println("map[" + change.getKey() + "]->X");
            }
        }));
        observableMap.put("A", "C");
        observableMap.put("A", "B");
        observableMap.put("A", "C");
        observableMap.put("A", "C");
        observableMap.put("A", "C");
    }

}
