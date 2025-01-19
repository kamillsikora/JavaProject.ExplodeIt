package com.example.app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {

    private Character player1Character;
    private Character player2Character;
    private ImageView player1Image;
    private ImageView player2Image;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final List<Rectangle> blockRectangles = new ArrayList<>();
    private boolean gameOver = false;
    private final List<ImageView> activeBombs = new ArrayList<>();
    private final Set<ImageView> accessibleBombs = new HashSet<>();
    private Map selectedMap = null;
    private final List<Item> items = new ArrayList<>();


    @Override
    public void start(Stage stage) {
        // Main title label
        Label titleLabel = new Label("EXPLODE IT GAME");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox.setMargin(titleLabel, new Insets(20, 0, 0, 0)); // Add spacing to move it up

        // Instruction label
        Label instructionLabel = new Label("Please select your map to play the game.");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        // Map selection components
        Label mapLabel = new Label("Select a Map:");
        mapLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        ComboBox<String> mapComboBox = new ComboBox<>();
        mapComboBox.setPromptText("Available Maps");
        mapComboBox.setPrefWidth(300); // Increased width for better usability

        List<Map> maps = Map.fetchMaps();
        for (Map map : maps) {
            mapComboBox.getItems().add(map.getName());
        }

        Button selectMapButton = new Button("Next");
        selectMapButton.setStyle("-fx-font-size: 14px; -fx-background-color: #5cb85c; -fx-text-fill: white; -fx-padding: 10;");

        // Map selection panel
        VBox mapSelectionLayout = new VBox(10, mapLabel, mapComboBox, selectMapButton);
        mapSelectionLayout.setStyle(
                "-fx-padding: 20; -fx-alignment: center; -fx-background-color: #343a40; "
                        + "-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;"
        );
        mapSelectionLayout.setMaxWidth(350); // Panel width adjusted to fit the combo box

        // Exit button
        Button exitButton = new Button("Exit Game");
        exitButton.setStyle("-fx-font-size: 14px; -fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10;");
        VBox.setMargin(exitButton, new Insets(0, 0, 20, 0)); // Add spacing to move it down
        exitButton.setOnAction(event -> System.exit(0));

        // Main layout
        VBox mainLayout = new VBox(20, titleLabel, instructionLabel, mapSelectionLayout, exitButton);
        mainLayout.setStyle("-fx-background-color: black; -fx-padding: 30; -fx-alignment: center;");

        Scene mapSelectionScene = new Scene(mainLayout, 1200, 700);

        // Disable resizing and set the scene
        stage.setResizable(false);
        stage.setScene(mapSelectionScene);
        stage.setTitle("Explode It Game - Map Selector");
        stage.show();

        // Event for the "Next" button
        selectMapButton.setOnAction(event -> {
            String selectedMapName = mapComboBox.getValue();

            if (selectedMapName == null) {
                showAlert("Please select a map!");
            } else {
                selectedMap = null;
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
        // Title Label
        Label characterLabel = new Label("Player 1: Select Your Character");
        characterLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox.setMargin(characterLabel, new Insets(20, 0, 20, 0));

        // Horizontal display for characters
        HBox charactersDisplay = new HBox(20);
        charactersDisplay.setAlignment(Pos.CENTER);
        charactersDisplay.setStyle("-fx-padding: 20;");

        // Fetch and display characters
        List<Character> characters = Character.fetchCharacters();
        for (Character character : characters) {
            VBox characterTile = new VBox(10);
            characterTile.setAlignment(Pos.CENTER);
            characterTile.setStyle(
                    "-fx-background-color: #343a40; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; "
                            + "-fx-border-color: white; -fx-border-width: 2;"
            );

            // Character Image
            ImageView characterImage = new ImageView(new Image(getClass().getResource(character.getLook().getFront()).toExternalForm()));
            characterImage.setFitWidth(100);
            characterImage.setFitHeight(100);

            // Character Details
            Label characterDetails = new Label(
                    character.getName() + "\n" +
                            "Speed: " + character.getCharacterSpeed() + "\n" +
                            "Explosion Power: " + character.getExplodePower() + "\n" +
                            "Explosion Speed: " + character.getExplodeSpeed() + "\n" +
                            "Max Bombs: " + character.getMaxBombs()
            );
            characterDetails.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-text-alignment: center;");

            // Select Button
            Button selectCharacterButton = new Button("Select");
            selectCharacterButton.setStyle(
                    "-fx-font-size: 14px; -fx-background-color: #5cb85c; -fx-text-fill: white; -fx-padding: 8; -fx-border-radius: 5;"
            );
            selectCharacterButton.setOnAction(event -> {
                player1Character = character;
                showPlayer2CharacterSelection(stage, map);
            });

            // Add components to the tile
            characterTile.getChildren().addAll(characterImage, characterDetails, selectCharacterButton);

            // Add the tile to the horizontal display
            charactersDisplay.getChildren().add(characterTile);
        }

        // Layout for the page
        VBox characterSelectionLayout = new VBox(20, characterLabel, charactersDisplay);
        characterSelectionLayout.setStyle("-fx-background-color: black; -fx-padding: 30; -fx-alignment: center;");

        // Scene setup
        Scene characterSelectionScene = new Scene(characterSelectionLayout, 1200, 700);
        stage.setScene(characterSelectionScene);
    }


    private void showPlayer2CharacterSelection(Stage stage, Map map) {
        // Title Label
        Label characterLabel = new Label("Player 2: Select Your Character");
        characterLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox.setMargin(characterLabel, new Insets(20, 0, 20, 0));

        // Horizontal display for characters
        HBox charactersDisplay = new HBox(20);
        charactersDisplay.setAlignment(Pos.CENTER);
        charactersDisplay.setStyle("-fx-padding: 20;");

        // Fetch and display characters
        List<Character> characters = Character.fetchCharacters();
        for (Character character : characters) {
            VBox characterTile = new VBox(10);
            characterTile.setAlignment(Pos.CENTER);
            characterTile.setStyle(
                    "-fx-background-color: #343a40; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; "
                            + "-fx-border-color: white; -fx-border-width: 2;"
            );

            // Character Image
            ImageView characterImage = new ImageView(new Image(getClass().getResource(character.getLook().getFront()).toExternalForm()));
            characterImage.setFitWidth(100);
            characterImage.setFitHeight(100);

            // Character Details
            Label characterDetails = new Label(
                    character.getName() + "\n" +
                            "Speed: " + character.getCharacterSpeed() + "\n" +
                            "Explosion Power: " + character.getExplodePower() + "\n" +
                            "Explosion Speed: " + character.getExplodeSpeed() + "\n" +
                            "Max Bombs: " + character.getMaxBombs()
            );
            characterDetails.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-text-alignment: center;");

            // Select Button
            Button selectCharacterButton = new Button("Select");
            selectCharacterButton.setStyle(
                    "-fx-font-size: 14px; -fx-background-color: #5cb85c; -fx-text-fill: white; -fx-padding: 8; -fx-border-radius: 5;"
            );
            selectCharacterButton.setOnAction(event -> {
                player2Character = character;
                showMapWithCharacters(stage, map);
            });

            // Add components to the tile
            characterTile.getChildren().addAll(characterImage, characterDetails, selectCharacterButton);

            // Add the tile to the horizontal display
            charactersDisplay.getChildren().add(characterTile);
        }

        // Layout for the page
        VBox characterSelectionLayout = new VBox(20, characterLabel, charactersDisplay);
        characterSelectionLayout.setStyle("-fx-background-color: black; -fx-padding: 30; -fx-alignment: center;");

        // Scene setup
        Scene characterSelectionScene = new Scene(characterSelectionLayout, 1200, 700);
        stage.setScene(characterSelectionScene);
    }



    private final List<ImageView> bombs = new ArrayList<>();

    private void showMapWithCharacters(Stage stage, Map map) {
        Pane layout = new Pane();

        // Set the background based on the map
        String colorOrImage = map.getColor().trim();
        if (colorOrImage.startsWith("http")) {
            layout.setStyle("-fx-background-image: url('" + colorOrImage + "'); -fx-background-size: cover;");
        } else {
            layout.setStyle("-fx-background-color: " + colorOrImage + ";");
        }

        // Calculate map height dynamically based on blocks
        int maxY = map.getBlocks().stream()
                .mapToInt(Block::getPositionY)
                .max()
                .orElse(0);
        int mapHeight = (maxY + 1) * 50;

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

        // Player statistics panels
        Label player1StatsLabel = new Label("Player1: Speed: " + player1Character.getCharacterSpeed() +
                " ExPower: " + player1Character.getExplodePower() +
                " MaxBombs: " + player1Character.getMaxBombs());
        player1StatsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Label player2StatsLabel = new Label("Player2: Speed: " + player2Character.getCharacterSpeed() +
                " ExPower: " + player2Character.getExplodePower() +
                " MaxBombs: " + player2Character.getMaxBombs());
        player2StatsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Player HP labels
        Label player1HpLabel = new Label("Player 1 HP: " + player1Character.getHp());
        Label player2HpLabel = new Label("Player 2 HP: " + player2Character.getHp());
        player1HpLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        player2HpLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Create the bottom panel
        HBox hpPanel = new HBox();
        hpPanel.setPadding(new Insets(10));
        hpPanel.setSpacing(50);
        hpPanel.setStyle("-fx-background-color: black;");
        hpPanel.setPrefHeight(50);
        hpPanel.setAlignment(Pos.CENTER);

        // Add statistics and HP labels to the panel
        VBox player1Panel = new VBox(player1StatsLabel, player1HpLabel);
        player1Panel.setSpacing(5);
        player1Panel.setAlignment(Pos.CENTER_LEFT);

        VBox player2Panel = new VBox(player2StatsLabel, player2HpLabel);
        player2Panel.setSpacing(5);
        player2Panel.setAlignment(Pos.CENTER_RIGHT);

        hpPanel.getChildren().addAll(player1Panel, new Pane(), player2Panel);
        HBox.setHgrow(hpPanel.getChildren().get(1), Priority.ALWAYS);

        // Create a BorderPane and set the map and panel
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout);
        borderPane.setBottom(hpPanel);

        // Calculate total height dynamically
        int panelHeight = 50;
        int totalHeight = mapHeight + panelHeight;

        Scene scene = new Scene(borderPane, 1200, totalHeight + 25);

        // Update stats and HP dynamically
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePlayerPositions(scene);
                player1HpLabel.setText("Player 1 HP: " + player1Character.getHp());
                player2HpLabel.setText("Player 2 HP: " + player2Character.getHp());
                player1StatsLabel.setText("Player1: Speed: " + player1Character.getCharacterSpeed() +
                        " ExPower: " + player1Character.getExplodePower() +
                        " MaxBombs: " + player1Character.getMaxBombs());
                player2StatsLabel.setText("Player2: Speed: " + player2Character.getCharacterSpeed() +
                        " ExPower: " + player2Character.getExplodePower() +
                        " MaxBombs: " + player2Character.getMaxBombs());
            }
        };
        timer.start();

        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            if (event.getCode() == KeyCode.SPACE) {
                dropBomb(player1Character, player1Image, layout);
            }
            if (event.getCode() == KeyCode.ENTER) {
                dropBomb(player2Character, player2Image, layout);
            }
        });

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        stage.setScene(scene);
    }




    private void dropBomb(Character character, ImageView player, Pane layout) {
        try {
            long activeBombCount = activeBombs.stream()
                    .filter(bomb -> bomb.getUserData() == character)
                    .count();

            if (activeBombCount >= character.getMaxBombs()) {
                return;
            }

            double bombX = Math.round(player.getLayoutX() / 50) * 50;
            double bombY = Math.round(player.getLayoutY() / 50) * 50;

            boolean bombAlreadyExists = activeBombs.stream()
                    .anyMatch(bomb -> bomb.getLayoutX() == bombX && bomb.getLayoutY() == bombY);
            if (bombAlreadyExists) {
                return;
            }

            Image bombImage = new Image(getClass().getResource(
                    "/org/example/explodeitapp/images/bomb.png").toExternalForm());
            ImageView bomb = new ImageView(bombImage);
            bomb.setFitWidth(50);
            bomb.setFitHeight(50);

            bomb.setLayoutX(bombX);
            bomb.setLayoutY(bombY);
            bomb.setUserData(character);

            layout.getChildren().add(bomb);
            activeBombs.add(bomb);
            accessibleBombs.add(bomb);

            accessibleBombs.add(bomb);

            int explodeSpeed = character.getExplodeSpeed();
            new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(
                            javafx.util.Duration.seconds(explodeSpeed),
                            event -> {
                                layout.getChildren().remove(bomb);
                                activeBombs.remove(bomb);
                                accessibleBombs.remove(bomb);

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
            return;
        }
        double sceneWidth = 1200;
        double sceneHeight = 700;
        double margin = 5;

        int player1Speed = player1Character.getCharacterSpeed();
        int player2Speed = player2Character.getCharacterSpeed();

        // Player 1 movement
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

        // Player 2 movement
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

        // Player 1 drops a bomb with SPACE
        if (pressedKeys.contains(KeyCode.SPACE)) {
            dropBomb(player1Character, player1Image, (Pane) scene.getRoot());
            pressedKeys.remove(KeyCode.SPACE);
        }

        // Player 2 drops a bomb with ENTER
        if (pressedKeys.contains(KeyCode.ENTER)) {
            dropBomb(player2Character, player2Image, (Pane) scene.getRoot());
            pressedKeys.remove(KeyCode.ENTER);
        }

        // Check collisions between players and flames
        checkFlameCollision();
        checkPlayerPickUpItem();
    }
    private long lastPlayer1DamageTime = 0;
    private long lastPlayer2DamageTime = 0;
    private void checkFlameCollision() {
        Pane layout = (Pane) player1Image.getParent();
        boolean player1Dead = false;
        boolean player2Dead = false;
        long currentTime = System.currentTimeMillis();

        for (var node : layout.getChildren()) {
            if (node instanceof ImageView) {
                ImageView flame = (ImageView) node;

                if (flame.getImage().getUrl().contains("flame")) { // Sprawdź, czy to płomień
                    if (player1Image.getBoundsInParent().intersects(flame.getBoundsInParent())) {
                        if (currentTime - lastPlayer1DamageTime >= 500) { // 0.5-second cooldown
                            player1Character.decrementHP();
                            lastPlayer1DamageTime = currentTime; // Update last damage time for Player 1
                            System.out.println("Player 1 HP: " + player1Character.getHp());
                            if (player1Character.getHp() == 0) {
                                player1Dead = true;
                            }
                        }
                    }
                    if (player2Image.getBoundsInParent().intersects(flame.getBoundsInParent())) {
                        if (currentTime - lastPlayer2DamageTime >= 500) { // 0.5-second cooldown
                            player2Character.decrementHP();
                            lastPlayer2DamageTime = currentTime; // Update last damage time for Player 2
                            System.out.println("Player 2 HP: " + player2Character.getHp());
                            if (player2Character.getHp() == 0) {
                                player2Dead = true;
                            }
                        }
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
            // Load the flame image
            Image flameImage = new Image(getClass().getResource("/org/example/explodeitapp/images/flame.png").toExternalForm());
            int explodePower = character.getExplodePower(); // Explosion range in tiles

            // Add flame at the bomb position
            addFlame(bombX, bombY, layout, flameImage);

            // Generate flames in each direction
            boolean blockedUp = false, blockedDown = false, blockedLeft = false, blockedRight = false;
            for (int i = 1; i <= explodePower; i++) {
                if (!blockedUp) {
                    blockedUp = addFlameWithBlockCheck(bombX, bombY - i * 50, layout, flameImage);
                }
                if (!blockedDown) {
                    blockedDown = addFlameWithBlockCheck(bombX, bombY + i * 50, layout, flameImage);
                }
                if (!blockedLeft) {
                    blockedLeft = addFlameWithBlockCheck(bombX - i * 50, bombY, layout, flameImage);
                }
                if (!blockedRight) {
                    blockedRight = addFlameWithBlockCheck(bombX + i * 50, bombY, layout, flameImage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addFlameWithBlockCheck(double x, double y, Pane layout, Image flameImage) {
        int posX = (int) (x / 50);
        int posY = (int) (y / 50);

        for (Block block : selectedMap.getBlocks()) {
            if (block.getPositionX() == posX && block.getPositionY() == posY) {
                // Stop flame propagation
                if (block instanceof DestructibleBlock || block instanceof LuckyBlock) {
                    checkAndDestroyBlock(x, y, layout); // Destroy block if destructible
                }
                return true;
            }
        }
        addFlame(x, y, layout, flameImage);
        return false;
    }


    private void checkAndDestroyBlock(double x, double y, Pane layout) {
        int posX = (int) (x / 50);
        int posY = (int) (y / 50);

        for (Block blockAtPosition : selectedMap.getBlocks()) {
            if (blockAtPosition.getPositionX() == posX && blockAtPosition.getPositionY() == posY) {
                if (blockAtPosition instanceof LuckyBlock || blockAtPosition instanceof DestructibleBlock) {
                    selectedMap.removeBlock(blockAtPosition);

                    blockRectangles.removeIf(rectangle -> {
                        return rectangle.getX() == blockAtPosition.getPositionX() * 50
                                && rectangle.getY() == blockAtPosition.getPositionY() * 50;
                    });

                    layout.getChildren().removeIf(node -> {
                        if (node instanceof Rectangle) {
                            Rectangle blockRect = (Rectangle) node;
                            return blockRect.getX() == blockAtPosition.getPositionX() * 50
                                    && blockRect.getY() == blockAtPosition.getPositionY() * 50;
                        }
                        return false;
                    });
                    if (blockAtPosition instanceof LuckyBlock) {
                        addItem(blockAtPosition.getPositionX() * 50, blockAtPosition.getPositionY() * 50, layout);
                    }

                    break;
                }
            }
        }
    }
    private void addItem(double x, double y, Pane layout) {
        int itemid = getRandomNumber();
        Item newItem = new Item(itemid, x, y);
        items.add(newItem);
        //image of the item being created and added
        Image itemImage = new Image(getClass().getResource(Item.getItemLook(itemid)).toExternalForm());
        // Create an ImageView for the item
        ImageView item = new ImageView(itemImage);
        item.setFitWidth(50);
        item.setFitHeight(50);
        item.setLayoutX(x);
        item.setLayoutY(y);
        // Add the item to the layout (map)
        layout.getChildren().add(item);
        player1Image.toFront();
        player2Image.toFront();

        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(8),
                        event -> layout.getChildren().remove(item)
                )
        ).play();
    }
    public static int getRandomNumber() {
        Random rand = new Random();
        double chance = rand.nextDouble();
        if (chance < 0.65) {
            return 1;
        } else if (chance < 0.90) {
            return 2;
        } else {
            return 3;
        }
    }

    private void checkPlayerPickUpItem() {
        Pane layout = (Pane) player1Image.getParent();
        List<Item> itemsToRemove = new ArrayList<>();

        for (Item item : items) {
            // Check if Player 1 intersects with the item
            if (player1Image.getBoundsInParent().intersects(item.getX(), item.getY(), 50, 50)) {
                System.out.println("Item picked up by Player 1!");
                player1Character.applyItemEffects(item);

                // Schedule reverting the effects after the item's duration
                new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(
                                javafx.util.Duration.seconds(item.getTimeOfEffect()),
                                event -> player1Character.revertToOriginalStats()
                        )
                ).play();
                itemsToRemove.add(item); // Mark for removal
            }

            // Check if Player 2 intersects with the item
            if (player2Image.getBoundsInParent().intersects(item.getX(), item.getY(), 50, 50)) {
                System.out.println("Item picked up by Player 2!");
                player2Character.applyItemEffects(item);

                new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(
                                javafx.util.Duration.seconds(item.getTimeOfEffect()),
                                event -> player2Character.revertToOriginalStats()
                        )
                ).play();

                itemsToRemove.add(item);
            }
        }

        // Remove picked-up items from the layout and the items list
        for (Item item : itemsToRemove) {
            layout.getChildren().removeIf(node -> node instanceof ImageView &&
                    node.getLayoutX() == item.getX() &&
                    node.getLayoutY() == item.getY());
            items.remove(item);
        }
    }

    private void addFlame(double x, double y, Pane layout, Image flameImage) {
        ImageView flame = new ImageView(flameImage);
        flame.setFitWidth(50);
        flame.setFitHeight(50);
        flame.setLayoutX(x);
        flame.setLayoutY(y);
        layout.getChildren().add(flame);

        if (checkPlayerOnFlame(player1Image, x, y)) {
            endGame("Player 2 wins!");
        } else if (checkPlayerOnFlame(player2Image, x, y)) {
            endGame("Player 1 wins!");
        }

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
        if (gameOver) return;
        gameOver = true;

        Platform.runLater(() -> {
            // Create a custom alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(winnerMessage);

            // Custom styling for the alert dialog
            alert.getDialogPane().setStyle(
                    "-fx-background-color: #dce2e7; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 20;"
            );

            // Apply custom styles directly to the buttons
            alert.getButtonTypes().forEach(buttonType -> {
                Button button = (Button) alert.getDialogPane().lookupButton(buttonType);
                button.setStyle("-fx-background-color: #343a40; -fx-text-fill: white; -fx-border-radius: 5;");
            });

            // When the alert is closed, redirect to the start screen
            alert.setOnHidden(event -> {
                System.exit(0);
            });
            alert.show();
        });
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
                return true;
            }
        }

        for (ImageView bomb : activeBombs) {
            if (bomb.getBoundsInParent().intersects(
                    nextX,
                    nextY,
                    player.getFitWidth(),
                    player.getFitHeight())) {

                if (accessibleBombs.contains(bomb)) {

                    if (!bomb.getBoundsInParent().intersects(
                            player.getLayoutX(),
                            player.getLayoutY(),
                            player.getFitWidth(),
                            player.getFitHeight())) {
                        accessibleBombs.remove(bomb);
                    }
                    return false;
                }
                return true;
            }
        }

        return false;
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
