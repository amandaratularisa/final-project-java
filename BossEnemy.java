package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BossEnemy extends GameObject implements Updatable {
    private final double minX;
    private final double maxX;
    private double velocityX;
    private final double moveSpeed;
    private final int maxHealth;
    private int health;
    private final List<BossProjectile> projectiles;
    private int attackTimer = 0;
    private final int attackInterval;
    private double targetX;
    private boolean alive = true;
    private double animationFrame = 0;
    private final ImageManager imageManager;
    private final Image bossImage;
    private boolean facingRight = true;
    
    public BossEnemy(double startX, double startY, double minX, double maxX, int levelNumber) {
        super(startX, startY, 90, 90);
        this.minX = minX;
        this.maxX = maxX;
        this.moveSpeed = 0;
        this.velocityX = 0;
        this.maxHealth = levelNumber >= 10 ? 6 : 3;
        this.health = maxHealth;
        this.attackInterval = levelNumber >= 10 ? 90 : 120;
        this.projectiles = new ArrayList<>();
        this.imageManager = ImageManager.getInstance();
        Image candidate = imageManager.getImage("enemies/boss");
        if (candidate == null) {
            candidate = imageManager.getImage("enemies/shadowlord");
        }
        if (candidate == null) {
            candidate = imageManager.getEnemyImage();
        }
        this.bossImage = candidate;
        this.targetX = startX;
    }
    
    public void setTargetX(double targetX) {
        this.targetX = targetX;
        this.facingRight = targetX >= (x + width / 2.0);
    }
    
    @Override
    public void update() {
        animationFrame += 0.08;
        if (alive) {
            attackTimer++;
            if (attackTimer >= attackInterval) {
                attackTimer = 0;
                launchProjectile();
            }
        }
        updateProjectiles();
    }
    
    public void update(Player player) {
        setTargetX(player.getX());
        update();
    }
    
    private void launchProjectile() {
        double direction = targetX >= x ? 1 : -1;
        double speed = 6;
        double projY = y + height - 25;
        projectiles.add(new BossProjectile(x + (direction > 0 ? width - 20 : 0), projY, direction * speed));
    }
    
    private void updateProjectiles() {
        Iterator<BossProjectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            BossProjectile projectile = iterator.next();
            projectile.update();
            if (!projectile.isActive()) {
                iterator.remove();
            }
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (alive) {
            drawBoss(gc);
        }
        drawProjectiles(gc);
    }
    
    private void drawBoss(GraphicsContext gc) {
        double bounce = Math.abs(Math.sin(animationFrame)) * 5;
        if (bossImage != null && imageManager.isImagesEnabled()) {
            double scale = 1.2;
            double drawWidth = width * scale;
            double drawHeight = height * scale;
            double drawX = x - (drawWidth - width) / 2;
            double drawY = y - bounce - (drawHeight - height);
            if (facingRight) {
                gc.drawImage(bossImage, drawX, drawY, drawWidth, drawHeight);
            } else {
                gc.drawImage(bossImage, drawX + drawWidth, drawY, -drawWidth, drawHeight);
            }
        } else {
            gc.setFill(Color.rgb(120, 20, 20));
            gc.fillOval(x, y - bounce, width, height);
            gc.setFill(Color.rgb(200, 60, 60));
            gc.fillOval(x + 15, y - bounce + 20, width - 30, height - 40);
            gc.setFill(Color.BLACK);
            gc.fillOval(x + 25, y - bounce + 30, 15, 18);
            gc.fillOval(x + width - 40, y - bounce + 30, 15, 18);
            gc.setFill(Color.WHITE);
            gc.fillRect(x + width / 2 - 25, y - bounce + 60, 50, 12);
        }
        drawHealthBar(gc);
    }
    
    private void drawHealthBar(GraphicsContext gc) {
        double percent = getHealthPercent();
        gc.setFill(Color.rgb(40, 0, 0, 0.7));
        gc.fillRoundRect(x + width / 2 - 50, y - 25, 100, 10, 8, 8);
        gc.setFill(Color.rgb(255, 60, 60));
        gc.fillRoundRect(x + width / 2 - 50, y - 25, 100 * percent, 10, 8, 8);
        gc.setStroke(Color.rgb(0, 0, 0, 0.8));
        gc.strokeRoundRect(x + width / 2 - 50, y - 25, 100, 10, 8, 8);
    }
    
    private void drawProjectiles(GraphicsContext gc) {
        for (BossProjectile projectile : projectiles) {
            projectile.draw(gc);
        }
    }
    
    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public double getHealthPercent() {
        return maxHealth == 0 ? 0 : (double) health / maxHealth;
    }
    
    public List<BossProjectile> getProjectiles() {
        return projectiles;
    }
    
    public static class BossProjectile extends GameObject implements Updatable {
        private double velocityX;
        private boolean active = true;

        public BossProjectile(double startX, double startY, double velocityX) {
            super(startX, startY, 28, 14);
            this.velocityX = velocityX;
        }

        @Override
        public void update() {
            if (!active) return;
            x += velocityX;
            if (x < -50 || x > 1300) {
                active = false;
            }
        }
        
        @Override
        public void draw(GraphicsContext gc) {
            if (!active) return;
            gc.setFill(Color.rgb(255, 140, 0));
            gc.fillOval(x, y, width, height);
            gc.setStroke(Color.rgb(255, 220, 50));
            gc.setLineWidth(2);
            gc.strokeOval(x, y, width, height);
        }
        
        public void deactivate() { active = false; }
        public boolean isActive() { return active; }
    }
}
