package com.game.mario;

/**
 * Interface Collidable - Contract untuk objek yang bisa bertabrakan
 * Demonstrasi: Interface, Abstraction, Polymorphism
 */
public interface Collidable {
    boolean collidesWith(double x, double y, double width, double height);
    double getX();
    double getY();
    double getWidth();
    double getHeight();
}
