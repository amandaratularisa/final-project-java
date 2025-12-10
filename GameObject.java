package com.game.mario;

import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract Class GameObject - Parent class untuk semua objek game
 * Demonstrasi: Abstraction, Inheritance, Encapsulation
 */
public abstract class GameObject implements Drawable, Collidable {
    protected double x, y;
    protected double width, height;
    
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    // Abstract method - harus diimplementasikan oleh child class
    @Override
    public abstract void draw(GraphicsContext gc);
    
    // Concrete method - collision detection
    @Override
    public boolean collidesWith(double otherX, double otherY, double otherWidth, double otherHeight) {
        return x < otherX + otherWidth &&
               x + width > otherX &&
               y < otherY + otherHeight &&
               y + height > otherY;
    }
    
    // Getter methods (Encapsulation)
    @Override
    public double getX() { return x; }
    
    @Override
    public double getY() { return y; }
    
    @Override
    public double getWidth() { return width; }
    
    @Override
    public double getHeight() { return height; }
    
    // Setter methods (Encapsulation)
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
}
