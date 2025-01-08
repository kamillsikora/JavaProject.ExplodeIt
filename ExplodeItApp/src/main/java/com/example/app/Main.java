package com.example.app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
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
    private boolean gameOver = false;
    private final List<ImageView> activeBombs = new ArrayList<>();
    private final Set<ImageView> accessibleBombs = new HashSet<>();
    private Map selectedMap = null;


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
                // Przypisanie wybranej mapy do zmiennej selectedMap
                selectedMap = null;
                for (Map map : maps) {
                    if (map.getName().equals(selectedMapName)) {
                        selectedMap = map;
                        break;
                    }
                }
                if (selectedMap != null) {
                    // Ładowanie bloków dla wybranej mapy
                    selectedMap.loadBlocks();

                    // Przechodzimy do wyboru gracza
                    showPlayer1CharacterSelection(stage, selectedMap);
                }
            }
        });
    }



        Label characterLabel = new Label(playerLabel);
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
            Label statsLabel = new Label(
                    "Speed: " + character.getCharacterSpeed() + "\n" +
                            "Explosion Power: " + character.getExplodePower() + "\n" +
                            "Explosion Speed: " + character.getExplodeSpeed() + "\n" +
                            "Max Bombs: " + character.getMaxBombs()
            );

            Button selectCharacterButton = new Button("Select");
            selectCharacterButton.setOnAction(event -> {
                if (isPlayer1) {
                    player1Character = character;
                    showCharacterSelection(stage, map, false); // Przejdź do wyboru gracza 2
                } else {
                    player2Character = character;
                    showMapWithCharacters(stage, map); // Rozpocznij grę
                }
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

            characterBox.getChildren().addAll(characterImage, characterDetails, statsLabel, selectCharacterButton);
            charactersDisplay.getChildren().add(characterBox);
        }

        VBox characterSelectionLayout = new VBox(10, characterLabel, charactersDisplay);
        characterSelectionLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene characterSelectionScene = new Scene(characterSelectionLayout, 1200, 700);
        stage.setScene(characterSelectionScene);
    }


    private final List<ImageView> bombs = new ArrayList<>(); // Lista bomb

    private void showMapWithCharacters(Stage stage, Map map) {
        Pane layout = new Pane();

        // Ustaw tło na podstawie mapy
        String colorOrImage = map.getColor().trim();
        if (colorOrImage.startsWith("http")) {
            layout.setStyle("-fx-background-image: url('" + colorOrImage + "'); -fx-background-size: cover;");
        } else {
            layout.setStyle("-fx-background-color: " + colorOrImage + ";");
        }

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
        player1Image.setLayoutX(5);
        player1Image.setLayoutY(5);

        // Display Player 2
        player2Image = new ImageView(new Image(getClass().getResource(player2Character.getLook().getFront()).toExternalForm()));
        player2Image.setFitWidth(40);
        player2Image.setFitHeight(40);
        player2Image.setLayoutX(1155);
        player2Image.setLayoutY(655);

        // Add players to the layout
        layout.getChildren().addAll(player1Image, player2Image);

        Scene scene = new Scene(layout, 1200, 700);

        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            if (event.getCode() == KeyCode.SPACE) {
                dropBomb(player1Character, player1Image, layout); // Przekazuje postać gracza 1
            }
            if (event.getCode() == KeyCode.ENTER) {
                dropBomb(player2Character, player2Image, layout); // Przekazuje postać gracza 2
            }
        });

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

    private void dropBomb(Character character, ImageView player, Pane layout) {
        try {
            // Sprawdź liczbę aktywnych bomb przypisanych do danego gracza
            long activeBombCount = activeBombs.stream()
                    .filter(bomb -> bomb.getUserData() == character)
                    .count();

            // Sprawdź, czy gracz nie przekroczył limitu bomb
            if (activeBombCount >= character.getMaxBombs()) {
                return; // Nie można postawić kolejnej bomby
            }

            // Oblicz pozycję bomby na siatce mapy
            double bombX = Math.round(player.getLayoutX() / 50) * 50;
            double bombY = Math.round(player.getLayoutY() / 50) * 50;

            // Sprawdź, czy na tej pozycji nie ma już bomby
            boolean bombAlreadyExists = activeBombs.stream()
                    .anyMatch(bomb -> bomb.getLayoutX() == bombX && bomb.getLayoutY() == bombY);
            if (bombAlreadyExists) {
                return; // Nie można postawić bomby w tym samym miejscu
            }

            // Wczytaj obraz bomby
            Image bombImage = new Image(getClass().getResource(
                    "/org/example/explodeitapp/images/bomb.png").toExternalForm());
            ImageView bomb = new ImageView(bombImage);
            bomb.setFitWidth(50);
            bomb.setFitHeight(50);

            double bombX = Math.round(player.getLayoutX() / 50) * 50;
            double bombY = Math.round(player.getLayoutY() / 50) * 50;

            bomb.setLayoutX(bombX);
            bomb.setLayoutY(bombY);
            bomb.setUserData(character); // Przypisz bombę do właściciela (gracza)

            // Dodaj bombę do layoutu i listy aktywnych bomb
            layout.getChildren().add(bomb);
            activeBombs.add(bomb);
            accessibleBombs.add(bomb); // Na początku bomba jest dostępna dla przejścia

            // Na początku bomba jest dostępna dla przejścia
            accessibleBombs.add(bomb);

            // Obsługa wybuchu bomby
            int explodeSpeed = character.getExplodeSpeed();
            new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(
                            javafx.util.Duration.seconds(explodeSpeed),
                            event -> {
                                // Usuń bombę po wybuchu
                                layout.getChildren().remove(bomb);
                                activeBombs.remove(bomb);
                                accessibleBombs.remove(bomb);

                                // Wywołaj eksplozję
                                triggerExplosion(character, bombX, bombY, layout);
                            }
                    )
            ).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void updatePlayerPositions(Scene scene) {
        if (gameOver) {
            return; // Nie pozwala na ruch po zakończeniu gry
        }

        double sceneWidth = 1200;
        double sceneHeight = 700;
        double margin = 5;

        int player1Speed = player1Character.getCharacterSpeed();
        int player2Speed = player2Character.getCharacterSpeed();

        // Obsługa ruchu gracza 1
        if (pressedKeys.contains(KeyCode.W)) {
            if (player1Image.getLayoutY() > margin && !checkCollision(player1Image, 0, -player1Speed)) {
                player1Image.setLayoutY(player1Image.getLayoutY() - player1Speed);
            }
        } else if (pressedKeys.contains(KeyCode.S)) {
            if (player1Image.getLayoutY() + player1Image.getFitHeight() < sceneHeight - margin &&
                    !checkCollision(player1Image, 0, player1Speed)) {
                player1Image.setLayoutY(player1Image.getLayoutY() + player1Speed);
            }
        }

        if (pressedKeys.contains(KeyCode.A)) {
            if (player1Image.getLayoutX() > margin && !checkCollision(player1Image, -player1Speed, 0)) {
                player1Image.setLayoutX(player1Image.getLayoutX() - player1Speed);
            }
        } else if (pressedKeys.contains(KeyCode.D)) {
            if (player1Image.getLayoutX() + player1Image.getFitWidth() < sceneWidth - margin &&
                    !checkCollision(player1Image, player1Speed, 0)) {
                player1Image.setLayoutX(player1Image.getLayoutX() + player1Speed);
            }
        }

        // Obsługa ruchu gracza 2
        if (pressedKeys.contains(KeyCode.UP)) {
            if (player2Image.getLayoutY() > margin && !checkCollision(player2Image, 0, -player2Speed)) {
                player2Image.setLayoutY(player2Image.getLayoutY() - player2Speed);
            }
        } else if (pressedKeys.contains(KeyCode.DOWN)) {
            if (player2Image.getLayoutY() + player2Image.getFitHeight() < sceneHeight - margin &&
                    !checkCollision(player2Image, 0, player2Speed)) {
                player2Image.setLayoutY(player2Image.getLayoutY() + player2Speed);
            }
        }

        if (pressedKeys.contains(KeyCode.LEFT)) {
            if (player2Image.getLayoutX() > margin && !checkCollision(player2Image, -player2Speed, 0)) {
                player2Image.setLayoutX(player2Image.getLayoutX() - player2Speed);
            }
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            if (player2Image.getLayoutX() + player2Image.getFitWidth() < sceneWidth - margin &&
                    !checkCollision(player2Image, player2Speed, 0)) {
                player2Image.setLayoutX(player2Image.getLayoutX() + player2Speed);
            }
        }

        // Gracz 1 rzuca bombę spacją
        if (pressedKeys.contains(KeyCode.SPACE)) {
            dropBomb(player1Character, player1Image, (Pane) scene.getRoot());
            pressedKeys.remove(KeyCode.SPACE);
        }

        // Gracz 2 rzuca bombę enterem
        if (pressedKeys.contains(KeyCode.ENTER)) {
            dropBomb(player2Character, player2Image, (Pane) scene.getRoot());
            pressedKeys.remove(KeyCode.ENTER);
        }

        // Sprawdzenie kolizji graczy z płomieniami
        checkFlameCollision();
    }

    private void checkFlameCollision() {
        Pane layout = (Pane) player1Image.getParent();
        boolean player1Dead = false;
        boolean player2Dead = false;

        for (var node : layout.getChildren()) {
            if (node instanceof ImageView) {
                ImageView flame = (ImageView) node;

                if (flame.getImage().getUrl().contains("flame")) { // Sprawdź, czy to płomień
                    if (player1Image.getBoundsInParent().intersects(flame.getBoundsInParent())) {
                        player1Dead = true;
                    }
                    if (player2Image.getBoundsInParent().intersects(flame.getBoundsInParent())) {
                        player2Dead = true;
                    }
                }
            }
        }

        if (player1Dead && player2Dead) {
            endGame("Draw! Both players lost!");
        } else if (player1Dead) {
            endGame("Player 2 wins!");
        } else if (player2Dead) {
            endGame("Player 1 wins!");
        }
    }


    private void triggerExplosion(Character character, double bombX, double bombY, Pane layout) {
        try {
            // Wczytaj obraz płomienia
            Image flameImage = new Image(getClass().getResource("/org/example/explodeitapp/images/flame.png").toExternalForm());

            int explodePower = character.getExplodePower(); // Moc wybuchu w kratkach


            // Dodaj płomień w miejscu bomby
            addFlame(bombX, bombY, layout, flameImage);
            checkAndDestroyBlock(bombX, bombY, layout); // Sprawdź i usuń blok w miejscu bomby

            // Generuj płomienie w każdą stronę
            for (int i = 1; i <= explodePower; i++) {
                double upY = bombY - i * 50;
                double downY = bombY + i * 50;
                double leftX = bombX - i * 50;
                double rightX = bombX + i * 50;

                // Dodaj płomienie w odpowiednich kierunkach
                if (upY >= 0) {
                    addFlame(bombX, upY, layout, flameImage);
                    checkAndDestroyBlock(bombX, upY, layout); // Sprawdź i usuń blok w górę
                }
                if (downY <= layout.getHeight()) {
                    addFlame(bombX, downY, layout, flameImage);
                    checkAndDestroyBlock(bombX, downY, layout); // Sprawdź i usuń blok w dół
                }
                if (leftX >= 0) {
                    addFlame(leftX, bombY, layout, flameImage);
                    checkAndDestroyBlock(leftX, bombY, layout); // Sprawdź i usuń blok w lewo
                }
                if (rightX <= layout.getWidth()) {
                    addFlame(rightX, bombY, layout, flameImage);
                    checkAndDestroyBlock(rightX, bombY, layout); // Sprawdź i usuń blok w prawo
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndDestroyBlock(double x, double y, Pane layout) {
        // Zaokrąglamy pozycje do wielokrotności 50 (zakładając, że każdy blok ma rozmiar 50x50)
        int posX = (int) (x / 50);
        int posY = (int) (y / 50);

        for (Block blockAtPosition : selectedMap.getBlocks()) {
            if (blockAtPosition.getPositionX() == posX && blockAtPosition.getPositionY() == posY) {
                if (blockAtPosition instanceof LuckyBlock || blockAtPosition instanceof DestructibleBlock) {
                    // Usuwamy blok z mapy logicznej
                    selectedMap.removeBlock(blockAtPosition);

                    // Usuwamy odpowiadający mu Rectangle z listy blockRectangles
                    blockRectangles.removeIf(rectangle -> {
                        // Usuwamy blok, jeśli jego pozycja w layout odpowiada usuniętemu blokowi
                        return rectangle.getX() == blockAtPosition.getPositionX() * 50
                                && rectangle.getY() == blockAtPosition.getPositionY() * 50;
                    });

                    // Usuwamy blok z layoutu
                    layout.getChildren().removeIf(node -> {
                        if (node instanceof Rectangle) {
                            Rectangle blockRect = (Rectangle) node;
                            return blockRect.getX() == blockAtPosition.getPositionX() * 50
                                    && blockRect.getY() == blockAtPosition.getPositionY() * 50;
                        }
                        return false;
                    });

                    break; // Przerywamy pętlę po usunięciu bloku
                }
            }
        }
    }


    private void addFlame(double x, double y, Pane layout, Image flameImage) {
        ImageView flame = new ImageView(flameImage);
        flame.setFitWidth(50);
        flame.setFitHeight(50);
        flame.setLayoutX(x);
        flame.setLayoutY(y);
        layout.getChildren().add(flame);

        // Sprawdź, czy płomień trafia w gracza
        if (checkPlayerOnFlame(player1Image, x, y)) {
            endGame("Player 2 wins!");
        } else if (checkPlayerOnFlame(player2Image, x, y)) {
            endGame("Player 1 wins!");
        }

        // Usuń płomień po 1 sekundzie
        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(1),
                        event -> layout.getChildren().remove(flame)
                )
        ).play();
    }

    private boolean checkPlayerOnFlame(ImageView player, double flameX, double flameY) {
        return player.getLayoutX() < flameX + 40 &&
                player.getLayoutX() + player.getFitWidth() > flameX &&
                player.getLayoutY() < flameY + 40 &&
                player.getLayoutY() + player.getFitHeight() > flameY;
    }

    private void endGame(String winnerMessage) {
        if (gameOver) return; // Jeśli gra już się zakończyła, pomiń
        gameOver = true; // Zatrzymaj grę

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(winnerMessage);

            alert.setOnHidden(event -> Platform.exit()); // Zamknij aplikację po zamknięciu alertu
            alert.show();
        });
    }

    private boolean checkCollision(ImageView player, int dx, int dy) {
        double nextX = player.getLayoutX() + dx;
        double nextY = player.getLayoutY() + dy;

        // Sprawdź kolizję z blokami
        for (Rectangle block : blockRectangles) {
            if (block.getBoundsInParent().intersects(
                    nextX,
                    nextY,
                    player.getFitWidth(),
                    player.getFitHeight())) {
                return true; // Blok znajduje się w drodze
            }
        }

        // Sprawdź kolizję z bombami
        for (ImageView bomb : activeBombs) {
            if (bomb.getBoundsInParent().intersects(
                    nextX,
                    nextY,
                    player.getFitWidth(),
                    player.getFitHeight())) {
                // Jeśli bomba jest dostępna, pozwól przejść
                if (accessibleBombs.contains(bomb)) {
                    // Usuń bombę z dostępnych, gdy gracz odejdzie
                    if (!bomb.getBoundsInParent().intersects(
                            player.getLayoutX(),
                            player.getLayoutY(),
                            player.getFitWidth(),
                            player.getFitHeight())) {
                        accessibleBombs.remove(bomb);
                    }
                    return false; // Brak kolizji
                }
                return true; // Kolizja z bombą
            }
        }

        return false; // Brak kolizji
    }

    private void showGameOverAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Ustaw akcję po zamknięciu alertu
        alert.setOnHidden(event -> Platform.runLater(() -> {
            // Zakończ aplikację
            Platform.exit();
        }));

        alert.show(); // Używamy `show()` zamiast `showAndWait()`
    }



    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    public static void main(String[] args) {
        launch();
    }
}
