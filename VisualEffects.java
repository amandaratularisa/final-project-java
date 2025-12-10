package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import java.util.Random;

public class VisualEffects {
    private static Random random = new Random();
    
    public static class Star {
        public double x, y, size, twinkleSpeed, twinkleOffset;
        
        public Star(double x, double y) {
            this.x = x;
            this.y = y;
            this.size = 1 + random.nextDouble() * 2;
            this.twinkleSpeed = 0.02 + random.nextDouble() * 0.03;
            this.twinkleOffset = random.nextDouble() * Math.PI * 2;
        }
        
        public void draw(GraphicsContext gc, double time) {
            double alpha = 0.5 + Math.sin(time * twinkleSpeed + twinkleOffset) * 0.5;
            gc.setFill(Color.rgb(255, 255, 255, alpha));
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }
    
    public static class Cloud {
        public double x, y, speed;
        public int size;
        private int width;
        
        public Cloud(double x, double y, int windowWidth) {
            this.x = x;
            this.y = y;
            this.width = windowWidth;
            this.speed = 0.3 + random.nextDouble() * 0.5;
            this.size = 60 + random.nextInt(40);
        }
        
        public void update() {
            x += speed;
            if (x > width + 100) x = -100;
        }
        
        public void draw(GraphicsContext gc) {
            gc.setFill(Color.rgb(255, 255, 255, 0.3));
            gc.fillOval(x, y, size, size * 0.6);
            gc.fillOval(x + size * 0.3, y - size * 0.2, size * 0.7, size * 0.5);
            gc.fillOval(x + size * 0.6, y, size * 0.8, size * 0.6);
        }
    }
    
    public static class Particle {
        public double x, y, vx, vy, life, maxLife;
        public Color color;
        
        public Particle(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.vx = (random.nextDouble() - 0.5) * 3;
            this.vy = (random.nextDouble() - 0.5) * 3;
            this.maxLife = 60 + random.nextInt(60);
            this.life = maxLife;
            this.color = color;
        }
        
        public void update() {
            x += vx;
            y += vy;
            vy += 0.1;
            life--;
        }
        
        public boolean isAlive() {
            return life > 0;
        }
        
        public void draw(GraphicsContext gc) {
            double alpha = life / maxLife;
            gc.setFill(Color.rgb((int)(color.getRed() * 255), 
                                  (int)(color.getGreen() * 255), 
                                  (int)(color.getBlue() * 255), alpha));
            gc.fillOval(x - 3, y - 3, 6, 6);
        }
    }
    
    public static LinearGradient createSkyGradient(double height) {
        return new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(25, 25, 60)),
            new Stop(0.5, Color.rgb(60, 40, 100)),
            new Stop(1, Color.rgb(120, 80, 160))
        );
    }
    
    public static LinearGradient createGameSkyGradient(double height) {
        return new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(135, 206, 250)),
            new Stop(1, Color.rgb(176, 224, 230))
        );
    }
    
    public static void drawGlowText(GraphicsContext gc, String text, double x, double y,
                                     Font font, Color color, Color glowColor) {
        gc.setFont(font);
        gc.setFill(glowColor);
        gc.fillText(text, x + 2, y + 2);
        gc.setFill(glowColor.deriveColor(0, 1, 1, 0.6));
        gc.fillText(text, x + 1, y + 1);
        gc.setFill(color);
        gc.fillText(text, x, y);
    }
}