package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BossWeaponPickup extends GameObject implements Updatable {
    private static final double GRAVITY = 0.4;  // Perlambat dari 0.6
    private static final int GROUND_DURATION = 300;  // 5 detik (60 fps * 5)
    private static final int WARNING_TIME = 120;  // Mulai blink 2 detik sebelum hilang
    
    private double velocityY = 0;
    private final double groundY;
    private boolean onGround = false;
    private boolean collected = false;
    private boolean expired = false;
    private int groundTimer = 0;
    
    public BossWeaponPickup(double startX, double startY, double groundY) {
        super(startX, startY, 30, 30);
        this.groundY = groundY;
    }
    
    @Override
    public void update() {
        if (collected || expired) return;
        
        if (!onGround) {
            // Fase jatuh - perlambat gravity
            velocityY += GRAVITY;
            y += velocityY;
            
            if (y + height >= groundY) {
                y = groundY - height;
                velocityY = 0;
                onGround = true;
                groundTimer = 0;  // Mulai timer saat mendarat
            }
        } else {
            // Fase di tanah - tunggu beberapa detik sebelum expired
            groundTimer++;
            if (groundTimer >= GROUND_DURATION) {
                expired = true;
            }
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (collected || expired) return;
        
        // Blink warning jika hampir expired
        boolean shouldBlink = onGround && groundTimer >= (GROUND_DURATION - WARNING_TIME);
        if (shouldBlink && (groundTimer / 10) % 2 == 0) {
            return;  // Skip drawing untuk efek blink
        }
        
        double glow = Math.abs(Math.sin(System.nanoTime() * 1e-9)) * 0.4 + 0.3;
        
        // Glow lebih besar jika sedang jatuh (lebih noticeable)
        double glowSize = !onGround ? 15 : 5;
        gc.setFill(Color.rgb(255, 200, 80, 0.8));
        gc.fillOval(x - glowSize, y - glowSize, width + glowSize * 2, height + glowSize * 2);
        
        gc.setFill(Color.rgb(255, 150, 50));
        gc.fillRoundRect(x, y, width, height, 8, 8);
        
        gc.setStroke(Color.rgb(255, 255, 255, glow));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, width, height, 8, 8);
        
        gc.setFill(Color.rgb(255, 255, 180));
        gc.fillPolygon(
            new double[] {x + width / 2, x + width - 6, x + width / 2 + 2, x + 6},
            new double[] {y + 4, y + height / 2, y + height - 4, y + height / 2},
            4
        );
    }
    
    public boolean isCollected() { return collected; }
    public void collect() { this.collected = true; }
    public boolean isAvailable() { return !collected && !expired; }
    public boolean shouldRemove() { return collected || expired; }
}
