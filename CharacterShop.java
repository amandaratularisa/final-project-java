package com.game.mario;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CharacterShop {
    
    public static class CharacterItem {
        String name;
        String description;
        int price;
        Color color;
        
        public CharacterItem(String name, String description, int price, Color color) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.color = color;
        }
    }
    
    private Stage stage;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private PlayerData playerData;
    private List<CharacterItem> characters;
    private Runnable onBackCallback;
    private static final String MENU_BACKGROUND_PATH = "assets/ui/opening_Background.png";
    private static final String PANEL_STYLE = "-fx-background-color: rgba(6, 12, 28, 0.92);" +
            "-fx-background-radius: 32;" +
            "-fx-border-color: rgba(98, 201, 255, 0.5);" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 32;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 35, 0.2, 0, 18);";
    private static final String CARD_STYLE = "-fx-background-color: linear-gradient(to bottom, rgba(22,32,66,0.96), rgba(10,16,34,0.95));" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: rgba(140, 214, 255, 0.35);" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 22;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 25, 0.18, 0, 10);";
    
    public CharacterShop(Stage stage, int width, int height, PlayerData playerData, Runnable onBackCallback) {
        this.stage = stage;
        this.playerData = playerData;
        this.onBackCallback = onBackCallback;
        initCharacters();
    }
    
    private void initCharacters() {
        characters = new ArrayList<>();
        characters.add(new CharacterItem("HERO", "The default brave hero", 0, Color.rgb(255, 100, 100)));
        characters.add(new CharacterItem("KNIGHT", "A strong armored knight", 100, Color.rgb(150, 150, 200)));
        characters.add(new CharacterItem("NINJA", "A fast and agile ninja", 200, Color.rgb(50, 50, 50)));
        characters.add(new CharacterItem("WIZARD", "A magical wizard with special powers", 300, Color.rgb(150, 50, 200)));
        characters.add(new CharacterItem("DRAGON", "A powerful dragon character", 500, Color.rgb(255, 50, 50)));
    }
    
    private void applyBackgroundImage(Region root) {
        System.out.println("CharacterShop: Applying background image with CSS...");
        
        try {
            // USE CSS -fx-background-image (MOST RELIABLE!)
            File imgFile = new File(MENU_BACKGROUND_PATH);
            String cssPath = imgFile.toURI().toString();
            
            System.out.println("CharacterShop: CSS Path = " + cssPath);
            System.out.println("CharacterShop: File exists = " + imgFile.exists());
            
            String cssStyle = String.format(
                "-fx-background-image: url('%s'); " +
                "-fx-background-size: %d %d; " +
                "-fx-background-position: center; " +
                "-fx-background-repeat: no-repeat;",
                cssPath, WIDTH, HEIGHT
            );
            
            root.setStyle(cssStyle);
            System.out.println("CharacterShop: ✓ CSS background applied!");
            
        } catch (Exception ex) {
            System.err.println("CharacterShop: Exception while applying CSS background!");
            ex.printStackTrace();
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
        }
    }
    
    public javafx.scene.Parent getRoot() {
        BorderPane root = new BorderPane();
        applyBackgroundImage(root);
        root.setPadding(new Insets(30));

        VBox panel = createOverlayPanel();

        VBox header = new VBox(6);
        header.setAlignment(Pos.CENTER);
        Label title = createNeonLabel("Character Shop", 34, Color.web("#f5f8ff"));

        HBox coinTag = new HBox(10);
        coinTag.setAlignment(Pos.CENTER);
        coinTag.setPadding(new Insets(10, 18, 10, 18));
        coinTag.setStyle("-fx-background-color: rgba(20,36,78,0.85); -fx-background-radius: 18; -fx-border-color: rgba(255,230,120,0.7); -fx-border-width: 1.5; -fx-border-radius: 18;");
        Label coinLabel = createNeonLabel("Coins: " + playerData.getTotalCoins(), 18, Color.web("#ffe066"));
        coinTag.getChildren().add(coinLabel);

        header.getChildren().addAll(title, coinTag);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        FlowPane charactersPane = new FlowPane();
        charactersPane.setHgap(20);
        charactersPane.setVgap(20);
        charactersPane.setAlignment(Pos.CENTER);
        charactersPane.setPrefWrapLength(900);
        charactersPane.setPadding(new Insets(10, 10, 30, 10));

        for (CharacterItem character : characters) {
            charactersPane.getChildren().add(createCharacterCard(character));
        }

        scrollPane.setContent(charactersPane);

        panel.getChildren().addAll(header, scrollPane);
        root.setCenter(panel);

        Button backBtn = createPrimaryButton("Back To Menu");
        backBtn.setOnAction(e -> goBackToMenu());
        BorderPane.setAlignment(backBtn, Pos.CENTER);
        BorderPane.setMargin(backBtn, new Insets(20));
        root.setBottom(backBtn);

        return root;
    }
    
    private VBox createCharacterCard(CharacterItem character) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(240);
        card.setPadding(new Insets(20));
        card.setStyle(CARD_STYLE);

        StackPane previewWrapper = new StackPane();
        previewWrapper.setPrefSize(140, 140);
        previewWrapper.setStyle("-fx-background-color: rgba(255,255,255,0.04); -fx-background-radius: 70; -fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 1; -fx-border-radius: 70;");

        Region preview = new Region();
        preview.setPrefSize(90, 90);
        preview.setStyle("-fx-background-color: " + toHex(character.color) + "; -fx-background-radius: 20;");
        previewWrapper.getChildren().add(preview);

        Label nameLabel = createNeonLabel(character.name, 20, Color.web("#f7fbff"));

        Label descLabel = new Label(character.description);
        descLabel.setWrapText(true);
        descLabel.setFont(GameFonts.of(13));
        descLabel.setTextFill(Color.web("#9cb8ff"));
        descLabel.setOpacity(0.9);

        Label priceLabel = createNeonLabel(character.price + " Coins", 16,
                character.price == 0 ? Color.web("#8bff9f") : Color.web("#ffe066"));

        HBox badges = new HBox(8);
        badges.setAlignment(Pos.CENTER);
        if (playerData.ownsCharacter(character.name)) {
            badges.getChildren().add(createBadge(playerData.getCurrentCharacter().equals(character.name) ? "Equipped" : "Owned",
                    playerData.getCurrentCharacter().equals(character.name) ? Color.web("#7cffa9") : Color.web("#7bc0ff")));
        }

        Button actionBtn;
        if (playerData.ownsCharacter(character.name)) {
            if (character.name.equals(playerData.getCurrentCharacter())) {
                actionBtn = createPrimaryButton("Equipped");
                actionBtn.setDisable(true);
            } else {
                actionBtn = createPrimaryButton("Equip");
                actionBtn.setOnAction(e -> equipCharacter(character));
            }
        } else {
            actionBtn = createAccentButton("Buy", Color.web("#ff7fd1"), Color.web("#ffb36c"));
            actionBtn.setOnAction(e -> buyCharacter(character));
        }

        card.getChildren().addAll(previewWrapper, nameLabel, descLabel, priceLabel);
        if (!badges.getChildren().isEmpty()) {
            card.getChildren().add(badges);
        }
        card.getChildren().add(actionBtn);
        return card;
    }
    
    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", 
            (int)(color.getRed() * 255), 
            (int)(color.getGreen() * 255), 
            (int)(color.getBlue() * 255));
    }
    
    private Button createPrimaryButton(String text) {
        Button button = new Button(text.toUpperCase());
        button.setPrefWidth(220);
        button.setPrefHeight(48);
        button.setFont(GameFonts.semiBold(16));
        String base = "-fx-background-color: linear-gradient(to right, #1f7bff, #23d5ff);" +
                "-fx-background-radius: 18; -fx-border-color: rgba(255,255,255,0.35); -fx-border-radius: 18; -fx-border-width: 1.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 18, 0.2, 0, 6); -fx-text-fill: #f6fdff; -fx-cursor: hand;";
        String hover = "-fx-background-color: linear-gradient(to right, #20d1ff, #76ffde);" +
                "-fx-background-radius: 18; -fx-border-color: rgba(255,255,255,0.45); -fx-border-radius: 18; -fx-border-width: 1.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 22, 0.25, 0, 8); -fx-text-fill: #ffffff; -fx-cursor: hand;";
        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(base));
        return button;
    }

    private Button createAccentButton(String text, Color start, Color end) {
        Button button = new Button(text.toUpperCase());
        button.setPrefWidth(200);
        button.setPrefHeight(44);
        button.setFont(GameFonts.semiBold(15));
        String base = String.format("-fx-background-color: linear-gradient(to right, %s, %s); -fx-background-radius: 16; " +
                        "-fx-border-color: rgba(255,255,255,0.25); -fx-border-radius: 16; -fx-border-width: 1.2; -fx-text-fill: #ffffff; -fx-cursor: hand;",
                toHex(start), toHex(end));
        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setOpacity(0.9));
        button.setOnMouseExited(e -> button.setOpacity(1));
        return button;
    }

    private VBox createOverlayPanel() {
        VBox panel = new VBox(20);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(new Insets(30));
        panel.setStyle(PANEL_STYLE);
        panel.setMaxWidth(980);
        panel.setMinHeight(520);
        return panel;
    }

    private Label createNeonLabel(String text, double size, Color color) {
        Label label = new Label(text);
        label.setFont(GameFonts.semiBold(size));
        label.setTextFill(color);
        return label;
    }

    private Label createBadge(String text, Color color) {
        Label badge = new Label(text.toUpperCase());
        badge.setFont(GameFonts.bold(11));
        badge.setTextFill(color);
        badge.setStyle(String.format("-fx-background-color: rgba(255,255,255,0.08); -fx-border-color: %s; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 4 10 4 10;", toHex(color)));
        return badge;
    }
    
    private void buyCharacter(CharacterItem character) {
        if (playerData.getTotalCoins() >= character.price) {
            if (playerData.spendCoins(character.price)) {
                playerData.unlockCharacter(character.name);
                playerData.save();
                
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Purchase Successful");
                success.setHeaderText("Character Unlocked!");
                success.setContentText("You successfully bought " + character.name + "!");
                success.showAndWait();
                
                // Refresh screen
                Scene scene = new Scene(getRoot(), WIDTH, HEIGHT);
                stage.setScene(scene);
                scene.getRoot().requestFocus();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Not Enough Coins");
            error.setHeaderText("Purchase Failed");
            error.setContentText("You need " + character.price + " coins but only have " + playerData.getTotalCoins() + " coins.");
            error.showAndWait();
        }
    }
    
    private void equipCharacter(CharacterItem character) {
        playerData.setCurrentCharacter(character.name);
        playerData.save();
        
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Character Equipped");
        success.setHeaderText("Success!");
        success.setContentText(character.name + " is now your active character!");
        success.showAndWait();
        
        // Refresh screen
        Scene scene = new Scene(getRoot(), WIDTH, HEIGHT);
        stage.setScene(scene);
        scene.getRoot().requestFocus();
    }
    
    private void goBackToMenu() {
        if (onBackCallback != null) {
            onBackCallback.run();
        } else {
            MainMenuUI menuUI = new MainMenuUI(stage);
            menuUI.showCharacterSelectedMenu(playerData);
        }
    }
}

