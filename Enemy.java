package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy class - Demonstrasi: Inheritance, Polymorphism, Encapsulation, Inner Class
 * Extends GameObject (Inheritance)
 * Implements Updatable (Polymorphism via Interface)
 */
public class Enemy extends GameObject implements Updatable {
    private double velocityX;
    private boolean alive = true;
    private double moveSpeed = 1.5;
    private double minX, maxX;
    private double animationFrame = 0;
    private static final double VISUAL_OFFSET_Y = -5;
    
    private ImageManager imageManager;
    private javafx.scene.image.Image enemyImage;
    
    public Enemy(double startX, double startY, double minX, double maxX) {
        super(startX, startY, 35, 35);
        this.minX = minX;
        this.maxX = maxX;
        this.velocityX = -moveSpeed;
        this.imageManager = ImageManager.getInstance();
        this.enemyImage = imageManager.getEnemyImage();
    }
    
    // Override from Updatable interface (Polymorphism)
    
    @Override
    public void update() {
        if (!alive) return;
        
        x += velocityX;
        animationFrame += 0.15;
        
        if (x <= minX) {
            x = minX;
            velocityX = moveSpeed;
        } else if (x >= maxX) {
            x = maxX;
            velocityX = -moveSpeed;
        }
    }
    
    // Override from GameObject (Polymorphism)
    @Override
    public void draw(GraphicsContext gc) {
        if (!alive) return;
        
        // AUTO-DETECT: Pakai gambar kalau ada, fallback ke shapes kalau tidak
        if (enemyImage != null) {
            // Render dengan gambar
            boolean facingRight = velocityX > 0;
            double bounce = Math.abs(Math.sin(animationFrame)) * 3;
            double renderY = y + VISUAL_OFFSET_Y - bounce;
            
            if (facingRight) {
                gc.drawImage(enemyImage, x, renderY, width, height);
            } else {
                // Flip horizontal
                gc.save();
                gc.scale(-1, 1);
                gc.drawImage(enemyImage, -x - width, renderY, width, height);
                gc.restore();
            }
        } else {
            // Fallback: render dengan shapes (original)
            drawWithShapes(gc);
        }
    }
    
    private void drawWithShapes(GraphicsContext gc) {
        boolean facingRight = velocityX > 0;
        double bounce = Math.abs(Math.sin(animationFrame)) * 3;
        double eyeBlink = Math.sin(animationFrame * 2) > 0.9 ? 2 : 0;
        
        drawLegs(gc, x, y + height - 5, animationFrame);
        
        drawBody(gc, x, y - bounce);
        
        drawEyes(gc, x, y - bounce, facingRight, eyeBlink);
        
        drawMouth(gc, x, y - bounce);
        
        drawHorns(gc, x, y - bounce);
        
        drawArms(gc, x, y - bounce + 15, animationFrame);
    }
    
    private void drawBody(GraphicsContext gc, double bx, double by) {
        gc.setFill(Color.rgb(80, 20, 100));
        gc.fillOval(bx, by + 5, width, height - 5);
        
        gc.setFill(Color.rgb(100, 40, 120));
        gc.fillOval(bx + 5, by + 8, width - 10, height - 15);
        
        gc.setFill(Color.rgb(60, 10, 80));
        for (int i = 0; i < 3; i++) {
            gc.fillOval(bx + 8 + i * 8, by + 20, 5, 5);
        }
    }
    
    private void drawEyes(GraphicsContext gc, double bx, double by, boolean right, double blink) {
        gc.setFill(Color.rgb(255, 50, 50));
        gc.fillOval(bx + 7, by + 10, 8, 10 - blink);
        gc.fillOval(bx + 20, by + 10, 8, 10 - blink);
        
        gc.setFill(Color.BLACK);
        if (right) {
            gc.fillOval(bx + 10, by + 12, 3, 4 - blink);
            gc.fillOval(bx + 23, by + 12, 3, 4 - blink);
        } else {
            gc.fillOval(bx + 8, by + 12, 3, 4 - blink);
            gc.fillOval(bx + 21, by + 12, 3, 4 - blink);
        }
        
        gc.setFill(Color.rgb(255, 100, 100, 0.5));
        gc.fillOval(bx + 8, by + 11, 2, 2);
        gc.fillOval(bx + 21, by + 11, 2, 2);
    }
    
    private void drawMouth(GraphicsContext gc, double bx, double by) {
        gc.setFill(Color.rgb(40, 0, 0));
        gc.fillArc(bx + 8, by + 18, 20, 12, 180, 180, javafx.scene.shape.ArcType.CHORD);
        
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            double tx = bx + 10 + i * 5;
            double[] xPoints = {tx, tx + 3, tx + 1.5};
            double[] yPoints = {by + 22, by + 22, by + 26};
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        
        gc.setFill(Color.rgb(200, 50, 50));
        gc.fillOval(bx + 13, by + 24, 10, 4);
    }
    
    private void drawHorns(GraphicsContext gc, double bx, double by) {
        gc.setFill(Color.rgb(60, 10, 80));
        
        double[] leftHornX = {bx + 5, bx + 2, bx + 8};
        double[] leftHornY = {by + 5, by - 5, by + 2};
        gc.fillPolygon(leftHornX, leftHornY, 3);
        
        double[] rightHornX = {bx + 30, bx + 33, bx + 27};
        double[] rightHornY = {by + 5, by - 5, by + 2};
        gc.fillPolygon(rightHornX, rightHornY, 3);
        
        gc.setFill(Color.rgb(100, 40, 120));
        gc.fillOval(bx + 3, by - 3, 4, 6);
        gc.fillOval(bx + 28, by - 3, 4, 6);
    }
    
    private void drawArms(GraphicsContext gc, double bx, double by, double frame) {
        double armSwing = Math.sin(frame) * 5;
        
        gc.setStroke(Color.rgb(80, 20, 100));
        gc.setLineWidth(4);
        gc.strokeLine(bx + 3, by, bx - 2, by + 8 + armSwing);
        gc.strokeLine(bx + 32, by, bx + 37, by + 8 - armSwing);
        
        gc.setFill(Color.rgb(60, 10, 80));
        gc.fillOval(bx - 5, by + 6 + armSwing, 6, 6);
        gc.fillOval(bx + 34, by + 6 - armSwing, 6, 6);
        
        gc.setFill(Color.rgb(255, 255, 255));
        gc.fillOval(bx - 4, by + 7 + armSwing, 2, 3);
        gc.fillOval(bx - 2, by + 7 + armSwing, 2, 3);
        gc.fillOval(bx + 35, by + 7 - armSwing, 2, 3);
        gc.fillOval(bx + 37, by + 7 - armSwing, 2, 3);
    }
    
    private void drawLegs(GraphicsContext gc, double bx, double by, double frame) {
        double legSwing = Math.sin(frame) * 3;
        
        gc.setStroke(Color.rgb(80, 20, 100));
        gc.setLineWidth(5);
        gc.strokeLine(bx + 10, by, bx + 8 - legSwing, by + 8);
        gc.strokeLine(bx + 25, by, bx + 27 + legSwing, by + 8);
        
        gc.setFill(Color.rgb(60, 10, 80));
        gc.fillOval(bx + 5 - legSwing, by + 6, 7, 5);
        gc.fillOval(bx + 23 + legSwing, by + 6, 7, 5);
        
        gc.setFill(Color.rgb(100, 40, 120));
        gc.fillOval(bx + 6 - legSwing, by + 7, 2, 2);
        gc.fillOval(bx + 8 - legSwing, by + 7, 2, 2);
        gc.fillOval(bx + 24 + legSwing, by + 7, 2, 2);
        gc.fillOval(bx + 26 + legSwing, by + 7, 2, 2);
    }
    
    public void kill() {
        alive = false;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    // Getters inherited from GameObject (x, y, width, height)
    
    /**
     * Inner Class - AIBehavior
     * Demonstrasi: Inner Class, Encapsulation
     */
    public class AIBehavior {
        public String getCurrentState() {
            if (!alive) return "Dead";
            if (velocityX < 0) return "Patrolling Left";
            return "Patrolling Right";
        }
        
        public double getAggressionLevel() {
            return moveSpeed / 3.0 * 100;
        }
        
        public boolean isAtEdge() {
            return x <= minX || x >= maxX;
        }
    }
    
    public AIBehavior getAI() {
        return new AIBehavior();
    }
}