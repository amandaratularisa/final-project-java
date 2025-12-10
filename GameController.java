package com.game.mario;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {
    private Canvas canvas;
    private GraphicsContext gc;
    private Player player;
    private GameLevel currentLevel;
    private StoryManager storyManager;
    private AnimationTimer gameLoop;
    private Set<KeyCode> pressedKeys;
    private Stage stage;
    private int currentLevelNumber = 1;
    private boolean showingStory = true;
    private StoryManager.StorySegment currentStory;
    private boolean gameWon = false;
    private boolean gameOver = false;
    private List<VisualEffects.Cloud> clouds;
    private List<VisualEffects.Particle> particles;
    private double time = 0;
    private PlayerData playerData;
    private ImageManager imageManager;
    private javafx.scene.image.Image backgroundImage;
    private javafx.scene.image.Image coinImage;
    private static final int MAX_HEARTS = 3;
    private int hearts = MAX_HEARTS;
    private final double spawnX = 50;
    private final double spawnY = 500;
    private boolean damageLock = false;
    private int coinsBankedThisRun = 0;
    private final AudioManager audioManager = AudioManager.getInstance();

    public GameController(Stage stage, int width, int height, PlayerData playerData) {
        this.stage = stage;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.pressedKeys = new HashSet<>();
        this.storyManager = new StoryManager();
        this.playerData = playerData;
        this.imageManager = ImageManager.getInstance();
        this.backgroundImage = imageManager.getBackgroundImage();
        this.coinImage = imageManager.getCoinImage(); // Cache coin image sekali saja

        initGame();
    }

    private void initGame() {
        hearts = MAX_HEARTS;
        player = createPlayer(spawnX, spawnY);
        currentLevel = new GameLevel(currentLevelNumber);
        currentStory = storyManager.getStoryForLevel(currentLevelNumber);
        coinsBankedThisRun = 0;

        clouds = new ArrayList<>();
        particles = new ArrayList<>();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 4; i++) {
            clouds.add(new VisualEffects.Cloud(random.nextDouble() * canvas.getWidth(),
                    30 + random.nextDouble() * 100,
                    (int) canvas.getWidth()));
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
    }

    private Player createPlayer(double startX, double startY) {
        Player newPlayer = new Player(startX, startY);
        newPlayer.setDamageListener(damage -> handlePlayerDamage(damage));
        return newPlayer;
    }

    private void update() {
        if (showingStory)
            return;

        if (gameWon)
            return;

        if (gameOver)
            return;

        boolean moveLeft = pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A);
        boolean moveRight = pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D);
        boolean jumpPressed = pressedKeys.contains(KeyCode.SPACE) || pressedKeys.contains(KeyCode.UP)
                || pressedKeys.contains(KeyCode.W);

        if (moveLeft) {
            player.moveLeft();
        }
        if (moveRight) {
            player.moveRight();
        }
        if (jumpPressed) {
            player.jump();
        }

        player.update();
        currentLevel.handleCollisions(player);

        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.update();
        }
        for (BossEnemy boss : currentLevel.getBosses()) {
            boss.update(player);
        }
        currentLevel.updateBossSystems(player);

        for (VisualEffects.Cloud cloud : clouds) {
            cloud.update();
        }

        particles.removeIf(p -> !p.isAlive());
        for (VisualEffects.Particle p : particles) {
            p.update();
        }

        if (currentLevel.isLevelComplete(player)) {
            audioManager.playEffect(AudioManager.SFX_LEVEL_CLEAR);
            hearts = MAX_HEARTS;
            if (currentLevelNumber < 10) {
                nextLevel();
            } else {
                winGame();
            }
        }

        damageLock = false;
        time += 0.05;
    }

    private void render() {
        // Render background - pakai gambar kalau ada, kalau tidak pakai gradient
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            LinearGradient skyGradient = VisualEffects.createGameSkyGradient(canvas.getHeight());
            gc.setFill(skyGradient);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Render clouds hanya kalau tidak ada background image
            for (VisualEffects.Cloud cloud : clouds) {
                cloud.draw(gc);
            }
        }

        if (showingStory) {
            renderStory();
            return;
        }

        if (gameOver) {
            renderGameOver();
            return;
        }

        if (gameWon) {
            renderVictory();
            return;
        }

        for (Platform platform : currentLevel.getPlatforms()) {
            platform.draw(gc);
        }

        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.draw(gc);
        }
        for (BossWeaponPickup pickup : currentLevel.getBossPickups()) {
            pickup.draw(gc);
        }
        for (HeroWeapon weapon : currentLevel.getHeroWeapons()) {
            weapon.draw(gc);
        }
        for (BossEnemy boss : currentLevel.getBosses()) {
            boss.draw(gc);
        }

        player.draw(gc);

        for (VisualEffects.Particle p : particles) {
            p.draw(gc);
        }

        renderHUD();
    }

    private void renderStory() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // Dimmed backdrop with subtle gradient streaks
        LinearGradient backdrop = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(3, 7, 20, 0.95)),
                new Stop(1, Color.rgb(6, 13, 32, 0.95)));
        gc.setFill(backdrop);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.rgb(34, 82, 140, 0.18));
        for (int i = 0; i < 6; i++) {
            double stripeY = i * 90 + (time * 8 % 90);
            gc.fillRect(0, stripeY, width, 35);
        }

        double panelWidth = width - 140;
        double panelHeight = 420;
        double panelX = 70;
        double panelY = (height - panelHeight) / 2;

        LinearGradient panelGradient = new LinearGradient(0, panelY, 0, panelY + panelHeight, false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(16, 25, 58, 0.96)),
                new Stop(1, Color.rgb(8, 12, 30, 0.96)));
        gc.setFill(panelGradient);
        gc.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 34, 34);

        gc.setStroke(Color.rgb(108, 232, 255, 0.65));
        gc.setLineWidth(3);
        gc.strokeRoundRect(panelX, panelY, panelWidth, panelHeight, 34, 34);

        // decorative glow orbs
        gc.setFill(Color.rgb(90, 160, 255, 0.25));
        gc.fillOval(panelX + panelWidth - 220, panelY - 80, 260, 260);
        gc.setFill(Color.rgb(255, 120, 189, 0.15));
        gc.fillOval(panelX - 60, panelY + panelHeight - 180, 200, 200);

        // Header tag
        double tagWidth = 220;
        gc.setFill(Color.rgb(24, 40, 74));
        gc.fillRoundRect(panelX + 50, panelY - 32, tagWidth, 34, 18, 18);
        gc.setStroke(Color.rgb(111, 234, 255));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(panelX + 50, panelY - 32, tagWidth, 34, 18, 18);
        gc.setFill(Color.rgb(181, 238, 255));
        gc.setFont(GameFonts.bold(16));
        gc.fillText("MISSION UPDATE", panelX + 70, panelY - 10);

        // Story title
        Font titleFont = GameFonts.extraBold(34);
        VisualEffects.drawGlowText(gc, currentStory.getTitle(), panelX + 50, panelY + 70,
                titleFont, Color.rgb(255, 240, 180), Color.rgb(255, 155, 68, 0.75));

        // Story text with bullet points
        String[] lines = currentStory.getText().split("\n");
        gc.setFont(GameFonts.regular(20));
        double textY = panelY + 140;
        for (String line : lines) {
            gc.setFill(Color.rgb(110, 220, 255));
            gc.fillOval(panelX + 55, textY - 8, 6, 6);
            gc.setFill(Color.rgb(235, 244, 255));
            gc.fillText(line, panelX + 75, textY);
            textY += 40;
        }

        // Divider
        gc.setStroke(Color.rgb(90, 120, 200, 0.6));
        gc.setLineWidth(1.4);
        gc.strokeLine(panelX + 40, panelY + panelHeight - 80, panelX + panelWidth - 40, panelY + panelHeight - 80);

        // Hint text
        gc.setFill(Color.rgb(175, 221, 255));
        gc.setFont(GameFonts.semiBold(18));
        gc.fillText("Collect coins to upgrade your hero in the shop!", panelX + 50, panelY + panelHeight - 40);

        // Prompt
        double pulse = 0.5 + 0.5 * Math.abs(Math.sin(time * 2.5));
        gc.setFont(GameFonts.bold(24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(120, 255, 255, 0.35 + 0.45 * pulse));
        gc.fillText("PRESS ENTER TO CONTINUE", width / 2.0, panelY + panelHeight + 40);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }

    private void renderVictory() {
        gc.setFill(Color.rgb(0, 20, 0, 0.95));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        java.util.Random random = new java.util.Random((long) (time * 100));
        for (int i = 0; i < 50; i++) {
            double x = random.nextDouble() * canvas.getWidth();
            double y = random.nextDouble() * canvas.getHeight();
            double size = 2 + random.nextDouble() * 3;
            gc.setFill(Color.rgb(255, 215, 0, 0.3 + random.nextDouble() * 0.7));
            gc.fillOval(x, y, size, size);
        }

        StoryManager.StorySegment victory = storyManager.getVictory();

        Font titleFont = GameFonts.bold(56);
        VisualEffects.drawGlowText(gc, victory.getTitle(), canvas.getWidth() / 2 - 200, 100,
                titleFont, Color.GOLD, Color.rgb(255, 215, 0, 0.8));

        gc.setFill(Color.rgb(20, 50, 20, 0.85));
        gc.fillRoundRect(30, 150, canvas.getWidth() - 60, 450, 30, 30);

        gc.setStroke(Color.rgb(100, 255, 100));
        gc.setLineWidth(3);
        gc.strokeRoundRect(30, 150, canvas.getWidth() - 60, 450, 30, 30);

        gc.setFill(Color.rgb(230, 250, 230));
        gc.setFont(GameFonts.regular(22));
        String[] lines = victory.getText().split("\n");
        int yPos = 210;
        for (String line : lines) {
            gc.fillText(line, 70, yPos);
            yPos += 45;
        }

        gc.setFill(Color.rgb(40, 80, 40, 0.9));
        gc.fillRoundRect(70, yPos + 20, 300, 60, 20, 20);

        gc.setStroke(Color.rgb(255, 215, 0));
        gc.setLineWidth(2);
        gc.strokeRoundRect(70, yPos + 20, 300, 60, 20, 20);

        Font coinsFont = GameFonts.bold(30);
        VisualEffects.drawGlowText(gc, "Total Coins: " + player.getCoins(), 90, yPos + 60,
                coinsFont, Color.rgb(255, 215, 0), Color.rgb(255, 140, 0, 0.6));

        double pulse = Math.abs(Math.sin(time * 2));
        Font promptFont = GameFonts.bold(24);
        VisualEffects.drawGlowText(gc, "Press ENTER or ESC to return to menu", 50, canvas.getHeight() - 40,
                promptFont, Color.rgb(150, 255, 150), Color.rgb(100, 200, 100, pulse));
    }

    private void renderHUD() {
        gc.setFill(Color.rgb(20, 20, 40, 0.85));
        gc.fillRoundRect(10, 10, 250, 130, 15, 15);

        gc.setStroke(Color.rgb(100, 100, 200));
        gc.setLineWidth(2);
        gc.strokeRoundRect(10, 10, 250, 130, 15, 15);

        Font hudFont = GameFonts.bold(20);
        gc.setFill(Color.rgb(100, 200, 255));
        gc.setFont(hudFont);
        gc.fillText("Level " + currentLevelNumber, 25, 38);

        drawHearts(25, 50);

        // Render coin dengan gambar jika ada (gunakan cached image)
        gc.setFill(Color.rgb(255, 215, 0));
        if (coinImage != null) {
            // Gambar coin + text
            double coinSize = 24;
            gc.drawImage(coinImage, 25, 95, coinSize, coinSize);
            gc.fillText("Coins: " + player.getCoins(), 55, 115);
        } else {
            // Fallback: text saja
            gc.fillText("Coins: " + player.getCoins(), 25, 115);
        }

        renderBossHUD();
    }

    private void drawHearts(double startX, double y) {
        javafx.scene.image.Image heartImg = imageManager.getHeartImage();

        for (int i = 0; i < MAX_HEARTS; i++) {
            double x = startX + i * 40;
            boolean filled = i < hearts;

            if (heartImg != null) {
                // Gunakan gambar heart
                double size = 32;
                if (filled) {
                    gc.drawImage(heartImg, x - 5, y - 5, size, size);
                } else {
                    // Gambar heart kosong (grayscale + transparansi)
                    gc.setGlobalAlpha(0.3);
                    gc.drawImage(heartImg, x - 5, y - 5, size, size);
                    gc.setGlobalAlpha(1.0);
                }
            } else {
                // Fallback: shapes (kode lama)
                Color fill = filled ? Color.rgb(255, 120, 140) : Color.rgb(90, 90, 90, 0.5);
                Color stroke = filled ? Color.rgb(255, 220, 230) : Color.rgb(140, 140, 140, 0.6);
                gc.setFill(fill);
                gc.setStroke(stroke);
                drawHeartShape(x, y, 18);
            }
        }
    }

    private void drawHeartShape(double x, double y, double size) {
        double radius = size / 2;
        gc.fillOval(x, y, radius, radius);
        gc.fillOval(x + radius, y, radius, radius);
        double[] heartX = { x - 2, x + radius * 2 + 2, x + radius };
        double[] heartY = { y + radius, y + radius, y + radius * 2.2 };
        gc.fillPolygon(heartX, heartY, 3);
        gc.strokeOval(x, y, radius, radius);
        gc.strokeOval(x + radius, y, radius, radius);
        gc.strokePolyline(new double[] { x - 2, x + radius, x + radius * 2 + 2 },
                new double[] { y + radius, y + radius * 2.2, y + radius }, 3);
    }

    private void renderBossHUD() {
        if (!currentLevel.hasBossFight() || !currentLevel.hasActiveBoss())
            return;
        double percent = currentLevel.getBossHealthPercent();
        double barWidth = 320;
        double barHeight = 24;
        double x = (canvas.getWidth() - barWidth) / 2;
        double y = 15;
        gc.setFill(Color.rgb(20, 0, 0, 0.85));
        gc.fillRoundRect(x, y, barWidth, barHeight, 15, 15);
        gc.setStroke(Color.rgb(255, 100, 100));
        gc.setLineWidth(3);
        gc.strokeRoundRect(x, y, barWidth, barHeight, 15, 15);
        gc.setFill(Color.rgb(255, 60, 60));
        gc.fillRoundRect(x + 10, y + 6, (barWidth - 20) * percent, barHeight - 12, 10, 10);
        String label = currentLevel.getLevelNumber() == 10 ? "Shadow Lord" : "Tower Guardian";
        gc.setFill(Color.rgb(255, 200, 200));
        gc.setFont(GameFonts.bold(18));
        gc.fillText("Boss: " + label, x + 15, y + barHeight + 25);
    }

    private void renderGameOver() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        Image bg = new Image(getClass().getResource("/backgrounds/bggameover.png").toExternalForm());
        gc.drawImage(bg, 0, 0, width, height);

    }

    private void handlePlayerDamage(int damage) {
        if (gameOver || gameWon || damageLock)
            return;
        damageLock = true;
        hearts--;
        if (hearts <= 0) {
            hearts = 0;
            triggerGameOver();
        } else {
            respawnPlayer();
        }
    }

    private void respawnPlayer() {
        int savedCoins = player.getCoins();
        player = createPlayer(spawnX, spawnY);
        player.setCoins(savedCoins);
        showingStory = false;
        pressedKeys.clear();
    }

    private void triggerGameOver() {
        if (gameOver)
            return;
        gameOver = true;
        if (playerData != null) {
            System.out.println("=== GAME OVER ===");
            int banked = bankNewCoins("game over");
            playerData.incrementGamesPlayed();
            playerData.updateHighScore(player.getCoins());
            playerData.save();
            System.out.println("Game over - banked " + banked + " new coins (total collected this run: "
                    + player.getCoins() + ")");
        } else {
            System.err.println("ERROR: playerData is null at game over!");
        }
    }

    private void returnToMenu() {
        System.out.println("Returning to Character Selected Menu...");
        stop();
        if (playerData != null) {
            System.out.println("Saving PlayerData - Player ID: " + playerData.getPlayerId());
            System.out.println("Current coins in PlayerData: " + playerData.getTotalCoins());
            playerData.save();
            System.out.println("PlayerData saved");
        } else {
            System.err.println("ERROR: playerData is null!");
        }

        javafx.application.Platform.runLater(() -> {
            if (playerData != null) {
                MainMenuUI menuUI = new MainMenuUI(stage);
                menuUI.showCharacterSelectedMenu(playerData);
                System.out.println("Switched to Character Selected Menu");
            } else {
                System.err.println("Cannot return to menu - playerData is null");
            }
        });
    }

    private void nextLevel() {
        if (playerData != null) {
            int coinsCollectedSoFar = player.getCoins();
            int newlyBanked = bankNewCoins("level " + currentLevelNumber + " complete");
            playerData.save();
            System.out.println("Level " + currentLevelNumber + " completed - banked " + newlyBanked
                    + " new coins (run total: " + coinsCollectedSoFar + ")");

            DatabaseManager db = DatabaseManager.getInstance();
            if (playerData.getPlayerId() > 0) {
                db.updateLevelProgress(playerData.getPlayerId(), currentLevelNumber, true, 0, 0, newlyBanked);
                System.out.println("Level progress saved to database");
            }
        }

        currentLevelNumber++;
        int coinsSoFar = player.getCoins();
        player = createPlayer(spawnX, spawnY);
        player.setCoins(coinsSoFar);
        hearts = MAX_HEARTS;
        currentLevel = new GameLevel(currentLevelNumber);
        currentStory = storyManager.getStoryForLevel(currentLevelNumber);
        showingStory = true;
    }

    private void winGame() {
        gameWon = true;
        showingStory = false;

        if (playerData != null) {
            int coinsCollected = player.getCoins();
            int banked = bankNewCoins("victory");
            playerData.incrementGamesPlayed();
            playerData.updateHighScore(coinsCollected);
            playerData.save();
            System.out.println("Victory! Banked " + banked + " new coins (run total: " + coinsCollected + ")");
        }

        for (int i = 0; i < 100; i++) {
            java.util.Random random = new java.util.Random();
            particles.add(new VisualEffects.Particle(
                    random.nextDouble() * canvas.getWidth(),
                    random.nextDouble() * canvas.getHeight(),
                    Color.GOLD));
        }
    }

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public Parent getRoot() {
        Pane root = new Pane();
        root.getChildren().add(canvas);
        root.setFocusTraversable(true);
        root.requestFocus();

        root.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                root.requestFocus();
            }
        });

        root.setOnKeyPressed(e -> {
            System.out.println("Key pressed: " + e.getCode() + " | GameOver: " + gameOver + " | GameWon: " + gameWon);
            pressedKeys.add(e.getCode());

            if (e.getCode() == KeyCode.ENTER && showingStory) {
                showingStory = false;
            }

            if (e.getCode() == KeyCode.ENTER && (gameOver || gameWon)) {
                System.out.println("ENTER pressed - Returning to menu");
                returnToMenu();
            }

            if (e.getCode() == KeyCode.ESCAPE) {
                System.out.println("ESC pressed - Returning to menu");
                returnToMenu();
            }
        });

        root.setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
        });

        return root;
    }

    private int bankNewCoins(String context) {
        if (playerData == null || player == null) {
            return 0;
        }
        int totalCollected = Math.max(0, player.getCoins());
        int newCoins = Math.max(0, totalCollected - coinsBankedThisRun);
        if (newCoins > 0) {
            playerData.addCoins(newCoins);
            coinsBankedThisRun += newCoins;
            System.out.println(
                    "Banked " + newCoins + " coins during " + context + " (total run coins: " + totalCollected + ")");
        } else {
            System.out.println("No new coins to bank during " + context + " (already banked: " + coinsBankedThisRun
                    + ", collected: " + totalCollected + ")");
        }
        return newCoins;
    }
}