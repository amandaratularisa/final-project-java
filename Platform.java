package com.game.mario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Platform extends GameObject {
    private Color color;
    private PlatformType type;
    private double collisionOffsetTop;
    
    private ImageManager imageManager;
    private javafx.scene.image.Image platformImage;
    private javafx.scene.image.Image coinImage;
    
    public enum PlatformType {
        GROUND, FLOATING, COIN
    }
    
    public Platform(double x, double y, double width, double height, PlatformType type) {
        super(x, y, width, height);
        this.type = type;
        this.imageManager = ImageManager.getInstance();
        
        if (type == PlatformType.FLOATING) {
            this.platformImage = imageManager.getFloatingBlockImage();
            this.collisionOffsetTop = 0;
        } else {
            this.platformImage = imageManager.getPlatformImage();
            this.collisionOffsetTop = 0;
        }
        this.coinImage = imageManager.getCoinImage();
        
        switch(type) {
            case GROUND:
                color = Color.BROWN;
                break;
            case FLOATING:
                color = Color.ORANGE;
                break;
            case COIN:
                color = Color.GOLD;
                break;
            default:
                color = Color.GRAY;
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        // AUTO-DETECT: Pakai gambar kalau ada, fallback ke shapes kalau tidak
        if (type == PlatformType.COIN) {
            // Render coin
            if (coinImage != null) {
                gc.drawImage(coinImage, x, y, width, height);
            } else {
                // Fallback: shapes
                gc.setFill(color);
                gc.fillRect(x, y, width, height);
                gc.setFill(Color.YELLOW);
                gc.fillOval(x + 5, y + 5, width - 10, height - 10);
            }
        } else {
            // Render platform
            if (platformImage != null) {
                // Render full gambar (tidak crop)
                gc.drawImage(platformImage, x, y, width, height);
            } else {
                // Fallback: shapes
                gc.setFill(color);
                gc.fillRect(x, y, width, height);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(x, y, width, height);
            }
        }
    }
    
    public boolean intersects(double px, double py, double pwidth, double pheight) {
        double collisionY = y + collisionOffsetTop;
        double collisionH = height - collisionOffsetTop;
        return px < x + width && px + pwidth > x && py < collisionY + collisionH && py + pheight > collisionY;
    }
    
    public double getCollisionTop() {
        return y + collisionOffsetTop;
    }
    
    public double getCollisionHeight() {
        return height - collisionOffsetTop;
    }
    
    public PlatformType getType() { return type; }
    
    public class PlatformProperties {
        public boolean isSolid() {
            return type == PlatformType.GROUND || type == PlatformType.FLOATING;
        }
        
        public boolean isCollectible() {
            return type == PlatformType.COIN;
        }
        
        public String getDescription() {
            switch(type) {
                case GROUND: return "Solid ground platform";
                case FLOATING: return "Floating platform in the air";
                case COIN: return "Collectible coin";
                default: return "Unknown platform";
            }
        }
        
        public double getSurfaceArea() {
            return width * height;
        }
    }
    
    public PlatformProperties getProperties() {
        return new PlatformProperties();
    }
}