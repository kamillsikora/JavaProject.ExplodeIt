package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label mapLabel = new Label("com.example.app.Map");

        ComboBox<String> mapComboBox = new ComboBox<>();
        mapComboBox.setPromptText("Select a com.example.app.Map");

        List<Map> maps = Map.fetchMaps();
        for (Map map : maps) {
            mapComboBox.getItems().add(map.getName());
        }

        Button acceptButton = new Button("Accept");

        acceptButton.setOnAction(event -> {
            String selectedMapName = mapComboBox.getValue();

            if (selectedMapName == null) {
                showAlert("Please select a map!");
            } else {
                for (Map map : maps) {
                    if (map.getName().equals(selectedMapName)) {
                        showMapWindow(map);
                        break;
                    }
                }
            }
        });

        // Layout
        VBox layout = new VBox(10, mapLabel, mapComboBox, acceptButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(layout, 1200, 700);

        stage.setScene(scene);
        stage.setTitle("com.example.app.Map Selector");
        stage.show();
    }


    private void showMapWindow(Map map) {
        Stage mapStage = new Stage();
        Pane layout = new Pane();

        map.loadBlocks();

        //Render blocks on the map
        for (Block block : map.getBlocks()) {
            Rectangle blockRect = new Rectangle(block.getPositionX() * 50, block.getPositionY() * 50, 50, 50);
            blockRect.setFill(javafx.scene.paint.Color.web(block.getColor()));
            layout.getChildren().add(blockRect);
        }

        //If the color is a URL, use it as a background image
        String colorOrImage = map.getColor();
        if (colorOrImage.startsWith("http")) {
            layout.setStyle("-fx-background-image: url('" + colorOrImage + "'); -fx-background-size: cover;");
        } else {
            layout.setStyle("-fx-background-color: " + colorOrImage + ";");
        }

        Scene scene = new Scene(layout, 1200, 700);
        mapStage.setTitle("com.example.app.Map Viewer");
        mapStage.setScene(scene);
        mapStage.show();
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
