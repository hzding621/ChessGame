package gui.example;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by haozhending on 9/18/16.
 */
public class Test {

    public static void main(String[] args) {

        Map<String, StringWrapper> map = new HashMap<>();
        ObservableMap<String, StringWrapper> observableMap = FXCollections.observableMap(map);
        observableMap.addListener((MapChangeListener<String, StringWrapper>) (change -> {
            if (change.wasRemoved()) {
                System.out.println("map[" + change.getKey() + "]->X");
            }
            if (change.wasAdded()) {
                System.out.println("map[" + change.getKey() + "]=" + change.getValueAdded());
            }
        }));
        observableMap.put("A", new StringWrapper("C"));
        observableMap.remove("A");
        observableMap.put("A", new StringWrapper("B"));
    }

    public static class StringWrapper {
        private final String s;

        public StringWrapper(String s) {
            this.s = s;
        }

        public String getS() {
            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringWrapper that = (StringWrapper) o;

            return s != null ? s.equals(that.s) : that.s == null;

        }

        @Override
        public int hashCode() {
            return s != null ? s.hashCode() : 0;
        }
    }

}
