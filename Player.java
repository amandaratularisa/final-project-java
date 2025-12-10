package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Player class - Demonstrasi: Inheritance, Polymorphism, Encapsulation
 * Extends GameObject (Inheritance)
 * Implements Updatable (Polymorphism via Interface)
 */
public class Player extends GameObject implements Updatable {
    private double velocityX, velocityY;
    private boolean isJumping = false;
    private boolean onGround = false;
    private boolean facingRight = true;
    private int health = 100;
    private int coins = 0;
    private double animationFrame = 0;
    
    private static final double GRAVITY = 0.8;
    private static final double JUMP_STRENGTH = -15;
    private static final double MOVE_SPEED = 3.2;
    private static final double MAX_FALL_SPEED = 15;
    private static final double VISUAL_OFFSET_Y = -10;
    private static final double IMAGE_SCALE = 1.15;
    private static final double IMAGE_OFFSET_Y = -2;
    
    private final ImageManager imageManager;
    private final Image playerImage;
    private final AudioManager audioManager = AudioManager.getInstance();
    private DamageListener damageListener;

    public interface DamageListener {
        void onDamage(int damage);
    }
    
    public Player(double startX, double startY) {
        super(startX, startY, 40, 50);
        this.imageManager = ImageManager.getInstance();
        this.playerImage = imageManager.getPlayerImage();
    }
    
    // Override from Updatable interface (Polymorphism)
    
    @Override
    public void update() {
        velocityY += GRAVITY;
        
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }
        
        y += velocityY;
        x += velocityX;
        
        velocityX *= 0.7;
        
        if (Math.abs(velocityX) > 0.5) {
            animationFrame += 0.3;
        }
    }
    
    public void jump() {
        if (onGround) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
            onGround = false;
            audioManager.playEffect(AudioManager.SFX_JUMP);
        }
    }
    
    public void moveLeft() {
        velocityX = -MOVE_SPEED;
        facingRight = false;
    }
    
    public void moveRight() {
        velocityX = MOVE_SPEED;
        facingRight = true;
    }

    public void setDamageListener(DamageListener listener) {
        this.damageListener = listener;
    }
    
    // Override from GameObject (Polymorphism)
    @Override
    public void draw(GraphicsContext gc) {
        if (playerImage != null && imageManager.isImagesEnabled()) {
            drawWithImage(gc);
        } else {
            drawWithShapes(gc);
        }
    }
    
    private void drawWithImage(GraphicsContext gc) {
        double drawWidth = width * IMAGE_SCALE;
        double drawHeight = height * IMAGE_SCALE;
        double drawX = x - (drawWidth - width) / 2;
        double drawY = y + height - drawHeight + IMAGE_OFFSET_Y;
        if (facingRight) {
            gc.drawImage(playerImage, drawX, drawY, drawWidth, drawHeight);
        } else {
            gc.drawImage(playerImage, drawX + drawWidth, drawY, -drawWidth, drawHeight);
        }
    }
    
    private void drawWithShapes(GraphicsContext gc) {
        double swing = Math.sin(animationFrame) * 4;
        double headX = x + width / 2 - 12;
        double headY = y - 2;
        double bodyX = x + width / 2 - 13;
        double bodyY = y + 18;
        
        drawLegsPixelStyle(gc, bodyX + 5, bodyY + 20, swing);
        drawBodyPixelStyle(gc, bodyX, bodyY);
        drawArmsPixelStyle(gc, bodyX - 6, bodyY + 4, swing);
        drawHeadPixelStyle(gc, headX, headY, facingRight);
        drawHairPixelStyle(gc, headX, headY);
    }

    
    private void drawHeadPixelStyle(GraphicsContext gc, double hx, double hy, boolean right) {
        gc.setFill(Color.rgb(255, 220, 177));
        gc.fillRoundRect(hx, hy, 24, 24, 6, 6);
        gc.setFill(Color.WHITE);
        if (right) {
            gc.fillRoundRect(hx + 11, hy + 11, 5, 5, 2, 2);
            gc.fillRoundRect(hx + 17, hy + 11, 5, 5, 2, 2);
            gc.setFill(Color.BLACK);
            gc.fillRect(hx + 13, hy + 13, 2, 3);
            gc.fillRect(hx + 19, hy + 13, 2, 3);
        } else {
            gc.fillRoundRect(hx + 4, hy + 11, 5, 5, 2, 2);
            gc.fillRoundRect(hx + 10, hy + 11, 5, 5, 2, 2);
            gc.setFill(Color.BLACK);
            gc.fillRect(hx + 6, hy + 13, 2, 3);
            gc.fillRect(hx + 12, hy + 13, 2, 3);
        }
        gc.setFill(Color.rgb(255, 170, 170));
        gc.fillRoundRect(hx + 6, hy + 17, 4, 2, 1, 1);
        gc.fillRoundRect(hx + 15, hy + 17, 4, 2, 1, 1);
    }
    
    private void drawHairPixelStyle(GraphicsContext gc, double hx, double hy) {
        gc.setFill(Color.rgb(40, 180, 110));
        gc.fillRoundRect(hx - 2, hy - 4, 28, 10, 8, 8);
        gc.fillRoundRect(hx, hy - 6, 20, 8, 6, 6);
        gc.setFill(Color.rgb(20, 120, 80));
        gc.fillOval(hx + 2, hy - 6, 6, 6);
        gc.fillOval(hx + 12, hy - 5, 6, 6);
        gc.fillOval(hx + 20, hy - 3, 6, 6);
    }
    
    private void drawBodyPixelStyle(GraphicsContext gc, double bx, double by) {
        gc.setFill(Color.rgb(50, 80, 200));
        gc.fillRoundRect(bx, by, 26, 26, 6, 6);
        gc.setFill(Color.rgb(10, 40, 160));
        gc.fillRoundRect(bx + 2, by + 2, 10, 22, 4, 4);
        gc.fillRoundRect(bx + 14, by + 2, 10, 22, 4, 4);
        gc.setFill(Color.rgb(255, 140, 0));
        gc.fillRoundRect(bx + 8, by + 10, 10, 8, 3, 3);
    }
    
    private void drawArmsPixelStyle(GraphicsContext gc, double ax, double ay, double swing) {
        gc.setFill(Color.rgb(200, 80, 20));
        gc.fillRoundRect(ax, ay + swing, 8, 20, 5, 5);
        gc.fillRoundRect(ax + width + 2, ay - swing, 8, 20, 5, 5);
        gc.setFill(Color.rgb(255, 220, 177));
        gc.fillRoundRect(ax, ay + 18 + swing, 8, 6, 3, 3);
        gc.fillRoundRect(ax + width + 2, ay + 18 - swing, 8, 6, 3, 3);
    }
    
    private void drawLegsPixelStyle(GraphicsContext gc, double lx, double ly, double swing) {
        gc.setFill(Color.rgb(240, 110, 20));
        gc.fillRoundRect(lx - 6, ly - swing, 10, 18, 4, 4);
        gc.fillRoundRect(lx + 8, ly + swing, 10, 18, 4, 4);
        gc.setFill(Color.rgb(0, 140, 140));
        gc.fillRoundRect(lx - 7, ly + 14 - swing, 12, 6, 3, 3);
        gc.fillRoundRect(lx + 7, ly + 14 + swing, 12, 6, 3, 3);
    }
    
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        if (damageListener != null) {
            damageListener.onDamage(damage);
        }
    }
    
    public void collectCoin() {
        coins++;
        audioManager.playEffect(AudioManager.SFX_COIN);
    }

    public void setCoins(int coins) {
        this.coins = Math.max(0, coins);
    }
    
    public boolean isAlive() {
        return health > 0;
    }
    
    // Getters (x, y, width, height inherited from GameObject)
    public double getVelocityY() { return velocityY; }
    public int getHealth() { return health; }
    public int getCoins() { return coins; }
    
    // Setters
    public void setVelocityY(double vy) { this.velocityY = vy; }
    public void setOnGround(boolean onGround) { 
        this.onGround = onGround;
        if (onGround) isJumping = false;
    }
    
    /**
     * Inner Class - PlayerStats
     * Demonstrasi: Inner Class, Encapsulation
     */
    public class PlayerStats {
        public int getTotalDamageDealt() {
            return 100 - health;
        }
        
        public double getJumpHeight() {
            return Math.abs(JUMP_STRENGTH);
        }
        
        public String getStatus() {
            if (health > 75) return "Excellent";
            if (health > 50) return "Good";
            if (health > 25) return "Injured";
            return "Critical";
        }
    }
    
    public PlayerStats getStats() {
        return new PlayerStats();
    }
}