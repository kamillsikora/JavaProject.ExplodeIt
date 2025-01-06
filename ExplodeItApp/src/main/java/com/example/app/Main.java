package com.example.app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    private Character player1Character;
    private Character player2Character;
    private ImageView player1Image;
    private ImageView player2Image;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean player1MovingVertically = false;
    private boolean player2MovingVertically = false;
    private final List<Rectangle> blockRectangles = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Label mapLabel = new Label("Map");

        ComboBox<String> mapComboBox = new ComboBox<>();
        mapComboBox.setPromptText("Select a Map");

        List<Map> maps = Map.fetchMaps();
        for (Map map : maps) {
            mapComboBox.getItems().add(map.getName());
        }

        Button selectMapButton = new Button("Next");

        VBox mapSelectionLayout = new VBox(10, mapLabel, mapComboBox, selectMapButton);
        mapSelectionLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene mapSelectionScene = new Scene(mapSelectionLayout, 1200, 700);

        stage.setScene(mapSelectionScene);
        stage.setTitle("Map Selector");
        stage.show();

        selectMapButton.setOnAction(event -> {
            String selectedMapName = mapComboBox.getValue();

            if (selectedMapName == null) {
                showAlert("Please select a map!");
            } else {
                Map selectedMap = null;
                for (Map map : maps) {
                    if (map.getName().equals(selectedMapName)) {
                        selectedMap = map;
                        break;
                    }
                }
                if (selectedMap != null) {
                    selectedMap.loadBlocks();
                    showPlayer1CharacterSelection(stage, selectedMap);
                }
            }
        });
    }

    private void showPlayer1CharacterSelection(Stage stage, Map map) {
        Label characterLabel = new Label("Player 1: Select Your Character");

        VBox charactersDisplay = new VBox(10);
        charactersDisplay.setAlignment(Pos.CENTER);

        List<Character> characters = Character.fetchCharacters();

        for (Character character : characters) {
            VBox characterBox = new VBox(5);
            characterBox.setAlignment(Pos.CENTER);

            ImageView characterImage = new ImageView(new Image(getClass().getResource(character.getLook().getFront()).toExternalForm()));
            characterImage.setFitWidth(50);
            characterImage.setFitHeight(50);

            Label characterDetails = new Label(character.getName());

            Button selectCharacterButton = new Button("Select");
            selectCharacterButton.setOnAction(event -> {
                player1Character = character;
                showPlayer2CharacterSelection(stage, map);
            });

            characterBox.getChildren().addAll(characterImage, characterDetails, selectCharacterButton);
            charactersDisplay.getChildren().add(characterBox);
        }

        VBox characterSelectionLayout = new VBox(10, characterLabel, charactersDisplay);
        characterSelectionLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene characterSelectionScene = new Scene(characterSelectionLayout, 1200, 700);
        stage.setScene(characterSelectionScene);
    }

    private void showPlayer2CharacterSelection(Stage stage, Map map) {
        Label characterLabel = new Label("Player 2: Select Your Character");

        VBox charactersDisplay = new VBox(10);
        charactersDisplay.setAlignment(Pos.CENTER);

        List<Character> characters = Character.fetchCharacters();

        for (Character character : characters) {
            VBox characterBox = new VBox(5);
            characterBox.setAlignment(Pos.CENTER);

            ImageView characterImage = new ImageView(new Image(getClass().getResource(character.getLook().getFront()).toExternalForm()));
            characterImage.setFitWidth(50);
            characterImage.setFitHeight(50);

            Label characterDetails = new Label(character.getName());

            Button selectCharacterButton = new Button("Select");
            selectCharacterButton.setOnAction(event -> {
                player2Character = character;
                showMapWithCharacters(stage, map);
            });

            characterBox.getChildren().addAll(characterImage, characterDetails, selectCharacterButton);
            charactersDisplay.getChildren().add(characterBox);
        }

        VBox characterSelectionLayout = new VBox(10, characterLabel, charactersDisplay);
        characterSelectionLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene characterSelectionScene = new Scene(characterSelectionLayout, 1200, 700);
        stage.setScene(characterSelectionScene);
    }

    private void showMapWithCharacters(Stage stage, Map map) {
        Pane layout = new Pane();

        // Render blocks on the map
        for (Block block : map.getBlocks()) {
            Rectangle blockRect = new Rectangle(block.getPositionX() * 50, block.getPositionY() * 50, 50, 50);
            blockRect.setFill(javafx.scene.paint.Color.web(block.getColor()));
            layout.getChildren().add(blockRect);
            blockRectangles.add(blockRect);
        }

        // Display Player 1
        player1Image = new ImageView(new Image(getClass().getResource(player1Character.getLook().getFront()).toExternalForm()));
        player1Image.setFitWidth(40);
        player1Image.setFitHeight(40);
        player1Image.setLayoutX(100);
        player1Image.setLayoutY(100);

        // Display Player 2
        player2Image = new ImageView(new Image(getClass().getResource(player2Character.getLook().getFront()).toExternalForm()));
        player2Image.setFitWidth(40);
        player2Image.setFitHeight(40);
        player2Image.setLayoutX(200);
        player2Image.setLayoutY(100);

        layout.getChildren().addAll(player1Image, player2Image);

        Scene scene = new Scene(layout, 1200, 700);

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePlayerPositions(scene);
            }
        };
        timer.start();

        stage.setScene(scene);
    }

    private void updatePlayerPositions(Scene scene) {
        double sceneWidth = 1200; // Szerokość okna
        double sceneHeight = 700; // Wysokość okna

        // Player 1 movement
        if (pressedKeys.contains(KeyCode.W)) {
            if (player1Image.getLayoutY() > 0 && !checkCollision(player1Image, 0, -5)) {
                player1Image.setLayoutY(player1Image.getLayoutY() - 5);
                player1MovingVertically = true;
            }
        } else if (pressedKeys.contains(KeyCode.S)) {
            if (player1Image.getLayoutY() + player1Image.getFitHeight() < sceneHeight && !checkCollision(player1Image, 0, 5)) {
                player1Image.setLayoutY(player1Image.getLayoutY() + 5);
                player1MovingVertically = true;
            }
        } else {
            player1MovingVertically = false;
        }

        if (pressedKeys.contains(KeyCode.A) && !player1MovingVertically) {
            if (player1Image.getLayoutX() > 0 && !checkCollision(player1Image, -5, 0)) {
                player1Image.setLayoutX(player1Image.getLayoutX() - 5);
            }
        } else if (pressedKeys.contains(KeyCode.D) && !player1MovingVertically) {
            if (player1Image.getLayoutX() + player1Image.getFitWidth() < sceneWidth && !checkCollision(player1Image, 5, 0)) {
                player1Image.setLayoutX(player1Image.getLayoutX() + 5);
            }
        }

        // Player 2 movement
        if (pressedKeys.contains(KeyCode.UP)) {
            if (player2Image.getLayoutY() > 0 && !checkCollision(player2Image, 0, -5)) {
                player2Image.setLayoutY(player2Image.getLayoutY() - 5);
                player2MovingVertically = true;
            }
        } else if (pressedKeys.contains(KeyCode.DOWN)) {
            if (player2Image.getLayoutY() + player2Image.getFitHeight() < sceneHeight && !checkCollision(player2Image, 0, 5)) {
                player2Image.setLayoutY(player2Image.getLayoutY() + 5);
                player2MovingVertically = true;
            }
        } else {
            player2MovingVertically = false;
        }

        if (pressedKeys.contains(KeyCode.LEFT) && !player2MovingVertically) {
            if (player2Image.getLayoutX() > 0 && !checkCollision(player2Image, -5, 0)) {
                player2Image.setLayoutX(player2Image.getLayoutX() - 5);
            }
        } else if (pressedKeys.contains(KeyCode.RIGHT) && !player2MovingVertically) {
            if (player2Image.getLayoutX() + player2Image.getFitWidth() < sceneWidth && !checkCollision(player2Image, 5, 0)) {
                player2Image.setLayoutX(player2Image.getLayoutX() + 5);
            }
        }
    }




    private boolean checkCollision(ImageView player, int dx, int dy) {
        double nextX = player.getLayoutX() + dx;
        double nextY = player.getLayoutY() + dy;

        for (Rectangle block : blockRectangles) {
            if (block.getBoundsInParent().intersects(
                    nextX,
                    nextY,
                    player.getFitWidth(),
                    player.getFitHeight())) {
                return true; // Blok znajduje się w drodze
            }
        }
        return false; // Brak kolizji
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
