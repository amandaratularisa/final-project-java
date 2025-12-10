package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HeroWeapon extends GameObject implements Updatable {
    private double velocityX;
    private boolean active = true;

    public HeroWeapon(double startX, double startY, double targetX) {
        super(startX, startY, 32, 14);
        double direction = targetX >= startX ? 1 : -1;
        this.velocityX = 9 * direction;
    }

    @Override
    public void update() {
        if (!active) return;
        x += velocityX;
        if (x < -100 || x > 1500) {
            active = false;
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(Color.rgb(200, 240, 255));
        gc.fillRoundRect(x, y, width, height, 6, 6);
        gc.setStroke(Color.rgb(120, 200, 255));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, width, height, 6, 6);
        gc.setFill(Color.rgb(255, 255, 255, 0.6));
        gc.fillOval(x + width - 6, y + 2, 6, height - 4);
    }
    
    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
}
