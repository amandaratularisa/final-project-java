package com.game.mario;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MainMenuUI {
    private final Stage stage;
    private final AudioManager audioManager = AudioManager.getInstance();
    private StackPane activeOverlayRoot;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final String MENU_BACKGROUND_PATH = "assets/ui/opening_Background.png";
    private static final String PANEL_STYLE = "-fx-background-color: rgba(6, 12, 28, 0.92);" +
            "-fx-background-radius: 28;" +
            "-fx-border-color: rgba(114, 201, 255, 0.5);" +
            "-fx-border-width: 1.4;" +
            "-fx-border-radius: 28;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 35, 0.2, 0, 18);";
    private static final String CARD_STYLE = "-fx-background-color: linear-gradient(to bottom, rgba(22,34,68,0.95), rgba(12,18,40,0.95));" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(146, 216, 255, 0.35);" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 18;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 25, 0.15, 0, 10);";

    public MainMenuUI(Stage stage) {
        this.stage = stage;
        ensureMenuMusic();
    }

    private void ensureMenuMusic() {
        audioManager.playMenuMusic();
    }

    private void applyBackgroundImage(Region root) {
        try {
            File imgFile = new File(MENU_BACKGROUND_PATH);
            String cssPath = imgFile.toURI().toString();
            String cssStyle = String.format(
                "-fx-background-image: url('%s'); -fx-background-size: %d %d; -fx-background-position: center; -fx-background-repeat: no-repeat;",
                cssPath, WIDTH, HEIGHT);
            root.setStyle(cssStyle);
        } catch (Exception ex) {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
        }
    }

    public Scene createWelcomeScreen() {
        ensureMenuMusic();
        VBox root = new VBox(22);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        applyBackgroundImage(root);
        applyFontFamily(root);

        Node title = createPixelTitle("PIXEL HEROES");

        Label subtitle = createNeonLabel("Select an option to begin", 18, Color.web("#cde7ff"));
        subtitle.setOpacity(0.85);

        Button newCharBtn = createStyledButton("New Character");
        Button loadCharBtn = createStyledButton("Load Character");
        Button leaderboardBtn = createStyledButton("Leaderboard");
        Button exitBtn = createStyledButton("Exit");

        newCharBtn.setOnAction(e -> showNewCharacterScreen());
        loadCharBtn.setOnAction(e -> showLoadCharacterScreen());
        leaderboardBtn.setOnAction(e -> showLeaderboardScreen(null));
        exitBtn.setOnAction(e -> System.exit(0));

        VBox buttons = new VBox(16);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(newCharBtn, loadCharBtn, leaderboardBtn, exitBtn);

        root.getChildren().addAll(title, subtitle, buttons);
        return new Scene(root, WIDTH, HEIGHT);
    }

    private void showNewCharacterScreen() {
        ensureMenuMusic();
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        applyBackgroundImage(root);
        applyFontFamily(root);

        Label title = createNeonLabel("New Character", 34, Color.web("#f5f8ff"));
        Label nameLabel = createNeonLabel("Character Name:", 18, Color.web("#a6c6ff"));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your character name");
        nameField.setMaxWidth(400);
        nameField.setFont(GameFonts.of(16));
        nameField.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-text-fill: #0f1a2e; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: rgba(255,255,255,0.35);");

        Button createBtn = createStyledButton("Create Character");
        Button backBtn = createStyledButton("Back");

        Label errorLabel = createNeonLabel("", 14, Color.web("#ff8b8b"));

        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Please enter a character name!");
                return;
            }

            DatabaseManager db = DatabaseManager.getInstance();
            int playerId = db.createPlayer(name);
            if (playerId > 0) {
                PlayerData playerData = PlayerData.load(name);
                showCharacterSelectedMenu(playerData);
            } else {
                errorLabel.setText("Character name already exists!");
            }
        });

        backBtn.setOnAction(e -> stage.setScene(createWelcomeScreen()));

        root.getChildren().addAll(title, nameLabel, nameField, createBtn, backBtn, errorLabel);
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
    }

    private void showLoadCharacterScreen() {
        ensureMenuMusic();
        BorderPane root = new BorderPane();
        applyBackgroundImage(root);
        root.setPadding(new Insets(30));
        applyFontFamily(root);

        Label title = createNeonLabel("Load Character", 34, Color.web("#f4f8ff"));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        DatabaseManager db = DatabaseManager.getInstance();
        List<DatabaseManager.PlayerRecord> characters = db.getAllPlayers();

        VBox panel = createOverlayPanel(940);
        panel.setAlignment(Pos.TOP_CENTER);

        if (characters.isEmpty()) {
            Label emptyLabel = createNeonLabel("No characters found. Create a new one!", 18, Color.web("#f8faff"));
            panel.getChildren().add(emptyLabel);
        } else {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
            scrollPane.setFitToWidth(true);

            VBox characterList = new VBox(14);
            characterList.setAlignment(Pos.TOP_CENTER);
            characterList.setPadding(new Insets(5, 5, 5, 5));

            for (DatabaseManager.PlayerRecord character : characters) {
                HBox charBox = new HBox(18);
                charBox.setAlignment(Pos.CENTER_LEFT);
                charBox.setStyle(CARD_STYLE);
                charBox.setPadding(new Insets(18));
                charBox.setPrefWidth(860);

                VBox infoBox = new VBox(4);
                Label nameLabel = createNeonLabel(character.getUsername(), 20, Color.web("#e8fbff"));
                Label statsLabel = createNeonLabel("Coins: " + character.getTotalCoins() + " | High Score: " + character.getHighScore(), 14, Color.web("#9bc4ff"));
                statsLabel.setOpacity(0.85);
                infoBox.getChildren().addAll(nameLabel, statsLabel);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button selectBtn = createGhostButton("Select", Color.web("#4df5ff"));
                selectBtn.setOnAction(e -> {
                    PlayerData playerData = PlayerData.load(character.getUsername());
                    showCharacterSelectedMenu(playerData);
                });

                Button deleteBtn = createGhostButton("Delete", Color.web("#ff8b8b"));
                deleteBtn.setOnAction(e -> {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Delete Character");
                    confirm.setHeaderText("Are you sure?");
                    confirm.setContentText("Delete: " + character.getUsername() + "?");

                    Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (db.deletePlayer(character.getId())) {
                            showLoadCharacterScreen();
                        }
                    }
                });

                charBox.getChildren().addAll(infoBox, spacer, selectBtn, deleteBtn);
                characterList.getChildren().add(charBox);
            }

            scrollPane.setContent(characterList);
            panel.getChildren().add(scrollPane);
        }

        root.setCenter(panel);

        Button backBtn = createStyledButton("Back");
        backBtn.setOnAction(e -> stage.setScene(createWelcomeScreen()));
        BorderPane.setAlignment(backBtn, Pos.CENTER);
        BorderPane.setMargin(backBtn, new Insets(20));
        root.setBottom(backBtn);

        stage.setScene(new Scene(root, WIDTH, HEIGHT));
    }

    private void showLeaderboardScreen(PlayerData returnTo) {
        ensureMenuMusic();
        BorderPane root = new BorderPane();
        applyBackgroundImage(root);
        root.setPadding(new Insets(30));
        applyFontFamily(root);

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);

        Label title = createNeonLabel("Leaderboard", 34, Color.web("#f4f8ff"));
        Label subtitle = createNeonLabel("Top 10 Players", 16, Color.web("#a6c6ff"));

        topBox.getChildren().addAll(title, subtitle);
        root.setTop(topBox);

        DatabaseManager db = DatabaseManager.getInstance();
        List<DatabaseManager.PlayerRecord> topPlayers = db.getTopPlayers(10);

        VBox panel = createOverlayPanel(940);
        panel.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        VBox leaderboard = new VBox(14);
        leaderboard.setAlignment(Pos.TOP_CENTER);
        leaderboard.setPadding(new Insets(6));

        if (topPlayers.isEmpty()) {
            leaderboard.getChildren().add(createNeonLabel("No players yet!", 18, Color.web("#f8faff")));
        } else {
            for (int i = 0; i < topPlayers.size(); i++) {
                DatabaseManager.PlayerRecord player = topPlayers.get(i);

                HBox rankBox = new HBox(24);
                rankBox.setAlignment(Pos.CENTER_LEFT);
                rankBox.setPadding(new Insets(20));
                rankBox.setPrefWidth(860);
                rankBox.setStyle(buildLeaderboardCardStyle(i));

                Label rankLabel = createNeonLabel("#" + (i + 1), 22, Color.web(getRankColor(i)));
                rankLabel.setMinWidth(70);

                Label nameLabel = createNeonLabel(player.getUsername(), 20, Color.web("#f3fbff"));
                nameLabel.setMinWidth(280);

                Label scoreLabel = createNeonLabel("Score: " + player.getHighScore(), 16, Color.web("#b3d8ff"));
                scoreLabel.setMinWidth(160);

                Label coinsLabel = createNeonLabel("Coins: " + player.getTotalCoins(), 16, Color.web("#b3d8ff"));

                rankBox.getChildren().addAll(rankLabel, nameLabel, scoreLabel, coinsLabel);
                leaderboard.getChildren().add(rankBox);
            }
        }

        scrollPane.setContent(leaderboard);
        panel.getChildren().add(scrollPane);
        root.setCenter(panel);

        Button backBtn = createStyledButton("Back");
        backBtn.setOnAction(e -> {
            if (returnTo != null) {
                showCharacterSelectedMenu(returnTo);
            } else {
                stage.setScene(createWelcomeScreen());
            }
        });
        BorderPane.setAlignment(backBtn, Pos.CENTER);
        BorderPane.setMargin(backBtn, new Insets(20));
        root.setBottom(backBtn);

        stage.setScene(new Scene(root, WIDTH, HEIGHT));
    }

    public void showCharacterSelectedMenu(PlayerData playerData) {
        ensureMenuMusic();
        PlayerData freshData = PlayerData.load(playerData.getUsername());

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        applyBackgroundImage(root);
        root.setPadding(new Insets(35));
        applyFontFamily(root);

        Label title = createNeonLabel("Welcome, " + freshData.getUsername() + "!", 30, Color.web("#f2f7ff"));

        DatabaseManager db = DatabaseManager.getInstance();
        int currentLevel = db.getCurrentLevel(freshData.getUsername());

        VBox statsBox = new VBox(8);
        statsBox.setAlignment(Pos.CENTER);

        // Coins dengan gambar
        HBox coinsBox = new HBox(8);
        coinsBox.setAlignment(Pos.CENTER);
        Image coinImage = ImageManager.getInstance().getCoinImage();
        if (coinImage != null) {
            ImageView coinIcon = new ImageView(coinImage);
            coinIcon.setFitWidth(24);
            coinIcon.setFitHeight(24);
            coinIcon.setPreserveRatio(true);
            coinsBox.getChildren().add(coinIcon);
        }
        Label coinsLabel = createNeonLabel("Coins: " + freshData.getTotalCoins(), 18, Color.web("#ffe066"));
        coinsBox.getChildren().add(coinsLabel);
        
        HBox scoreRow = new HBox(8);
        scoreRow.setAlignment(Pos.CENTER);
        Label scoreLabel = createNeonLabel("High Score: " + freshData.getHighScore(), 18, Color.web("#a6fffb"));
        Button infoBtn = createInfoButton();
        scoreRow.getChildren().addAll(scoreLabel, infoBtn);

        Label levelLabel = createNeonLabel("Current Level: " + currentLevel, 18, Color.web("#bdfb8c"));

        statsBox.getChildren().addAll(coinsBox, scoreRow, levelLabel);

        Button startGameBtn = createStyledButton("Start Game");
        Button shopBtn = createStyledButton("Character Shop");
        Button changeCharBtn = createStyledButton("Change Character");
        Button leaderboardBtn = createStyledButton("Leaderboard");
        Button exitBtn = createStyledButton("Exit");

        startGameBtn.setOnAction(e -> startGameSession(freshData));
        shopBtn.setOnAction(e -> {
            CharacterShop shop = new CharacterShop(stage, WIDTH, HEIGHT, freshData, () -> showCharacterSelectedMenu(freshData));
            Scene scene = new Scene(shop.getRoot(), WIDTH, HEIGHT);
            stage.setScene(scene);
            scene.getRoot().requestFocus();
            ensureMenuMusic();
        });
        changeCharBtn.setOnAction(e -> stage.setScene(createWelcomeScreen()));
        leaderboardBtn.setOnAction(e -> showLeaderboardScreen(freshData));
        exitBtn.setOnAction(e -> System.exit(0));

        root.getChildren().addAll(title, statsBox, startGameBtn, shopBtn, changeCharBtn, leaderboardBtn, exitBtn);

        StackPane layeredRoot = new StackPane(root);
        layeredRoot.setPickOnBounds(false);
        applyFontFamily(layeredRoot);
        activeOverlayRoot = layeredRoot;
        stage.setScene(new Scene(layeredRoot, WIDTH, HEIGHT));
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text.toUpperCase());
        btn.setPrefWidth(320);
        btn.setPrefHeight(54);
        btn.setFont(GameFonts.semiBold(18));
        btn.setTextFill(Color.web("#f6fdff"));
        String baseStyle = "-fx-background-color: linear-gradient(to right, #1f7bff, #23d5ff);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(255,255,255,0.35);" +
                "-fx-border-radius: 18;" +
                "-fx-border-width: 1.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 18, 0.2, 0, 6);" +
                "-fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: linear-gradient(to right, #20d1ff, #76ffde);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: rgba(255,255,255,0.45);" +
                "-fx-border-radius: 18;" +
                "-fx-border-width: 1.5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 22, 0.25, 0, 8);" +
                "-fx-cursor: hand;";
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    private Button createGhostButton(String text, Color borderColor) {
        Button btn = new Button(text.toUpperCase());
        btn.setFont(GameFonts.bold(14));
        btn.setTextFill(borderColor);
        String base = String.format("-fx-background-color: transparent; -fx-border-color: %s; -fx-border-width: 1.8; -fx-border-radius: 14; -fx-background-radius: 14; -fx-cursor: hand;", toHex(borderColor));
        String hover = String.format("-fx-background-color: rgba(255,255,255,0.08); -fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 14; -fx-background-radius: 14; -fx-cursor: hand;", toHex(borderColor.brighter()));
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    private Button createInfoButton() {
        Button infoBtn = new Button("?");
        infoBtn.setPrefSize(28, 28);
        infoBtn.setFont(GameFonts.bold(14));
        infoBtn.setTextFill(Color.web("#a6fffb"));
        String base = "-fx-background-color: transparent; -fx-border-color: rgba(166,255,251,0.7); -fx-border-radius: 50; -fx-background-radius: 50; -fx-cursor: hand;";
        String hover = "-fx-background-color: rgba(255,255,255,0.1); -fx-border-color: rgba(166,255,251,1); -fx-border-radius: 50; -fx-background-radius: 50; -fx-cursor: hand;";
        infoBtn.setStyle(base);
        infoBtn.setOnMouseEntered(e -> infoBtn.setStyle(hover));
        infoBtn.setOnMouseExited(e -> infoBtn.setStyle(base));
        infoBtn.setOnAction(e -> showHighScoreInfo());
        return infoBtn;
    }

    private void showHighScoreInfo() {
        StackPane overlayRoot = activeOverlayRoot;
        if (overlayRoot == null) {
            Scene scene = stage.getScene();
            if (scene == null) return;
            if (scene.getRoot() instanceof StackPane) {
                overlayRoot = (StackPane) scene.getRoot();
            } else {
                overlayRoot = new StackPane(scene.getRoot());
                scene.setRoot(overlayRoot);
            }
        }
        activeOverlayRoot = overlayRoot;

        StackPane layer = new StackPane();
        layer.setPadding(new Insets(26));
        layer.setStyle("-fx-background-color: rgba(4,8,20,0.65);");
        layer.setPickOnBounds(true);

        VBox card = new VBox(18);
        card.setPadding(new Insets(26));
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(20,32,68,0.98), rgba(10,15,34,0.98));" +
                "-fx-background-radius: 26;" +
                "-fx-border-color: rgba(120,215,255,0.55);" +
                "-fx-border-width: 1.6;" +
                "-fx-border-radius: 26;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 35, 0.35, 0, 14);");
        card.setPrefWidth(520);
        card.setMaxWidth(520);
        card.setFillWidth(true);
        applyFontFamily(card);

        Button closeBtn = new Button("✕");
        closeBtn.setFont(GameFonts.bold(12));
        closeBtn.setTextFill(Color.web("#ff8b8b"));
        closeBtn.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255,139,139,0.8); -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        StackPane finalOverlayRoot = overlayRoot;
        closeBtn.setOnAction(e -> finalOverlayRoot.getChildren().remove(layer));

        HBox closeRow = new HBox();
        closeRow.setAlignment(Pos.TOP_RIGHT);
        closeRow.getChildren().add(closeBtn);

        Label title = createNeonLabel("Penjelasan High Score", 26, Color.web("#f5fbff"));
        Label subtitle = createHighScoreInfoLabel("Rekor ini selalu nyimpen jumlah koin tertinggi dari SATU run (dari mulai main sampai kamu kalah atau menang). Cuma run itu yang dihitung, bukan akumulasi beberapa run.");

        HBox chips = new HBox(10);
        chips.setAlignment(Pos.CENTER_LEFT);
        chips.getChildren().addAll(
                createInfoChip("Koin Run"),
                createInfoChip("Game Over/Menang"),
                createInfoChip("Auto Update"));

        VBox explanations = new VBox(12);
        explanations.getChildren().addAll(
                createHighScoreInfoRow("🏁", "Run dimulai saat kamu tekan Start Game. Semua koin yang kamu kumpulkan sampai run selesai dihitung sebagai skor run itu."),
                createHighScoreInfoRow("🎯", "Begitu kamu kalah atau kelarin game, skor run dibandingin sama high score. Kalau lebih besar, langsung ganti rekor lama."),
                createHighScoreInfoRow("🔒", "Belanja di shop atau jumlah koin tabungan nggak ngaruh. High score cuma lihat koin dari run yang baru selesai."));

        VBox exampleBox = new VBox(6);
        exampleBox.setPadding(new Insets(14));
        exampleBox.setStyle("-fx-background-color: rgba(24,42,92,0.85); -fx-background-radius: 16; -fx-border-color: rgba(166,255,251,0.35); -fx-border-radius: 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 20, 0.2, 0, 6);");
        Label exampleTitle = createNeonLabel("Contoh Singkat", 18, Color.web("#a6fffb"));
        Label exampleLine1 = createHighScoreInfoLabel("Run #1: 90 koin → high score = 90.");
        Label exampleLine2 = createHighScoreInfoLabel("Run #2: 140 koin → high score naik ke 140.");
        Label exampleLine3 = createHighScoreInfoLabel("Run #3: 110 koin → rekor tetap 140 karena run ini lebih kecil.");
        exampleBox.getChildren().addAll(exampleTitle, exampleLine1, exampleLine2, exampleLine3);

        card.getChildren().addAll(closeRow, title, subtitle, chips, explanations, exampleBox);
        layer.getChildren().add(card);
        layer.setOnMouseClicked(e -> {
            if (e.getTarget() == layer) {
                finalOverlayRoot.getChildren().remove(layer);
            }
        });

        overlayRoot.getChildren().add(layer);
    }

    private Label createHighScoreInfoLabel(String text) {
        Label label = createNeonLabel(text, 15, Color.web("#eaf4ff"));
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_LEFT);
        label.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefWidth(Double.MAX_VALUE);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setOpacity(0.9);
        return label;
    }

    private HBox createHighScoreInfoRow(String icon, String text) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.TOP_LEFT);
        Label iconLabel = createNeonLabel(icon, 20, Color.web("#ffd782"));
        iconLabel.setMinWidth(26);
        Label desc = createHighScoreInfoLabel(text);
        desc.setPrefWidth(0);
        HBox.setHgrow(desc, Priority.ALWAYS);
        row.getChildren().addAll(iconLabel, desc);
        return row;
    }

    private Label createInfoChip(String text) {
        Label chip = new Label(text);
        chip.setFont(GameFonts.bold(12));
        chip.setTextFill(Color.web("#9cdcff"));
        chip.setStyle("-fx-background-color: rgba(55,105,204,0.4); -fx-background-radius: 999; -fx-padding: 6 14 6 14; -fx-border-color: rgba(156,220,255,0.7); -fx-border-radius: 999;");
        return chip;
    }

    private void startGameSession(PlayerData playerData) {
        GameController controller = new GameController(stage, WIDTH, HEIGHT, playerData);
        Scene scene = new Scene(controller.getRoot(), WIDTH, HEIGHT);
        applyFontFamily(scene.getRoot());
        stage.setScene(scene);
        scene.getRoot().requestFocus();
        controller.start();
    }

    private StackPane createPixelTitle(String text) {
        Canvas canvas = PixelFontRenderer.createTextCanvas(text.toUpperCase(), 14, PixelFontRenderer.TITLE, 6, 10);
        StackPane pane = new StackPane(canvas);
        pane.setPadding(new Insets(10));
        return pane;
    }

    private String getRankColor(int rank) {
        switch (rank) {
            case 0: return "#FFD700";
            case 1: return "#C0C0C0";
            case 2: return "#CD7F32";
            default: return "#87CEEB";
        }
    }

    private VBox createOverlayPanel(double maxWidth) {
        VBox panel = new VBox(18);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(new Insets(25));
        panel.setMaxWidth(maxWidth);
        panel.setStyle(PANEL_STYLE);
        return panel;
    }

    private Label createNeonLabel(String text, double size, Color color) {
        Label label = new Label(text);
        label.setFont(GameFonts.semiBold(size));
        label.setTextFill(color);
        return label;
    }

    private String buildLeaderboardCardStyle(int rank) {
        String glow = getRankColor(rank);
        return "-fx-background-color: linear-gradient(to right, rgba(15,25,54,0.96), rgba(12,20,46,0.96));" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: " + glow + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 25, 0.2, 0, 9);";
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }

    private void applyFontFamily(Node node) {
        if (node == null) return;
        String existing = node.getStyle();
        String fontCss = "-fx-font-family: '" + GameFonts.getFamily() + "', 'Bahnschrift', 'Arial';";
        if (existing == null || existing.isEmpty()) {
            node.setStyle(fontCss);
        } else {
            String separator = existing.trim().endsWith(";") ? " " : "; ";
            node.setStyle(existing + separator + fontCss);
        }
    }
}

