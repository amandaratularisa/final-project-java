package com.game.mario;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageManager {
    private static ImageManager instance = null;
    private Map<String, Image> imageCache;
    private boolean imagesEnabled = true;
    private static final String ASSET_PATH = "file:assets/";
    private Random random;
    
    private ImageManager() {
        this.imageCache = new HashMap<>();
        this.random = new Random();
        loadImages();
    }
    
    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }
    
    private void loadImages() {
        System.out.println("Loading images...");
        try {
            loadImagesFromFolder("characters");
            loadImagesFromFolder("enemies");
            loadImagesFromFolder("platforms");
            loadImagesFromFolder("backgrounds");
            loadImagesFromFolder("items");
            loadImagesFromFolder("ui"); 
            
            if (imageCache.size() > 0) {
                System.out.println("Loaded " + imageCache.size() + " images!");
                imageCache.keySet().forEach(key -> System.out.println("  - " + key));
            } else {
                System.out.println("No images found, using fallback shapes");
            }
        } catch (Exception e) {
            System.err.println("Error loading images");
            imagesEnabled = false;
        }
    }
    
    private void loadImagesFromFolder(String folderName) {
        try {
            File folder = new File("assets/" + folderName);
            if (!folder.exists()) {
                System.out.println("Folder not found: assets/" + folderName);
                return;
            }
            File[] files = folder.listFiles();
            if (files == null) return;
            for (File file : files) {
                String nameLower = file.getName().toLowerCase();
                if (nameLower.endsWith(".png") || nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg")) {
                    int dotIndex = file.getName().lastIndexOf('.');
                    String baseName = dotIndex > 0 ? file.getName().substring(0, dotIndex) : file.getName();
                    String key = folderName + "/" + baseName;
                    String fullPath = ASSET_PATH + folderName + "/" + file.getName();
                    try {
                        System.out.println("Loading: " + key + " from " + fullPath);
                        System.out.println("File size: " + file.length() + " bytes");
                        
                        // Load with background loading for large files
                        Image img = new Image(fullPath, false);
                        
                        System.out.println("Image created - Width: " + img.getWidth() + ", Height: " + img.getHeight());
                        System.out.println("Image error: " + img.isError() + ", Progress: " + img.getProgress());
                        
                        if (!img.isError()) {
                            if ("characters/Karakter".equals(key)) {
                                img = removeWhiteBackground(img);
                            }
                            imageCache.put(key, img);
                            System.out.println("✓ Successfully loaded: " + key);
                        } else {
                            System.err.println("✗ Image load error for: " + key);
                            if (img.getException() != null) {
                                System.err.println("  Exception: " + img.getException().getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("✗ Exception loading " + key + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in loadImagesFromFolder(" + folderName + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Image getImage(String key) { return imageCache.get(key); }
    public boolean hasImage(String key) { return imageCache.containsKey(key); }
    
    public Image getPlayerImage() {
        if (!imageCache.isEmpty()) {
            String[] preferred = {
                "characters/player",
                "characters/karakter",
                "characters/character",
                "characters/hero"
            };
            for (String desired : preferred) {
                for (String key : imageCache.keySet()) {
                    if (key.equalsIgnoreCase(desired)) {
                        return imageCache.get(key);
                    }
                }
            }
            for (String key : imageCache.keySet()) {
                if (key.toLowerCase().startsWith("characters/")) {
                    return imageCache.get(key);
                }
            }
        }
        return null;
    }
    
    public Image getEnemyImage() {
        List<Image> enemies = new ArrayList<>();
        for (String key : imageCache.keySet()) 
            if (key.startsWith("enemies/")) enemies.add(imageCache.get(key));
        if (enemies.isEmpty()) return null;
        return enemies.get(random.nextInt(enemies.size()));
    }
    
    public Image getPlatformImage() {
        if (imageCache.containsKey("platforms/ground")) return imageCache.get("platforms/ground");
        for (String key : imageCache.keySet()) 
            if (key.startsWith("platforms/")) return imageCache.get(key);
        return null;
    }
    
    public Image getFloatingBlockImage() {
        List<Image> blockImages = new ArrayList<>();
        
        if (imageCache.containsKey("platforms/block1")) blockImages.add(imageCache.get("platforms/block1"));
        if (imageCache.containsKey("platforms/block2")) blockImages.add(imageCache.get("platforms/block2"));
        if (imageCache.containsKey("platforms/block3")) blockImages.add(imageCache.get("platforms/block3"));
        
        if (imageCache.containsKey("platforms/block")) blockImages.add(imageCache.get("platforms/block"));
        if (imageCache.containsKey("platforms/floating")) blockImages.add(imageCache.get("platforms/floating"));
        
        if (!blockImages.isEmpty()) {
            return blockImages.get(random.nextInt(blockImages.size()));
        }
        
        return getPlatformImage();
    }
    
    public Image getCoinImage() {
        // Cari gambar coin spesifik
        if (imageCache.containsKey("items/coin")) return imageCache.get("items/coin");
        
        // Fallback: cari file yang mengandung "coin"
        for (String key : imageCache.keySet()) {
            if (key.toLowerCase().contains("coin")) return imageCache.get(key);
        }
        
        // Fallback terakhir: ambil gambar pertama di items/
        for (String key : imageCache.keySet()) {
            if (key.startsWith("items/")) return imageCache.get(key);
        }
        return null;
    }
    
    public Image getHeartImage() {
        // Cari gambar heart spesifik
        if (imageCache.containsKey("items/heart")) return imageCache.get("items/heart");
        
        // Fallback: cari file yang mengandung "heart" atau "hp" atau "health"
        for (String key : imageCache.keySet()) {
            String lowerKey = key.toLowerCase();
            if (lowerKey.contains("heart") || lowerKey.contains("hp") || lowerKey.contains("health")) {
                return imageCache.get(key);
            }
        }
        return null;
    }
    
    public Image getBackgroundImage() {
        for (String key : imageCache.keySet()) 
            if (key.startsWith("backgrounds/")) return imageCache.get(key);
        return null;
    }
    
    public boolean isImagesEnabled() { return imagesEnabled && imageCache.size() > 0; }

    private Image removeWhiteBackground(Image source) {
        try {
            int width = (int) source.getWidth();
            int height = (int) source.getHeight();
            WritableImage transparentImage = new WritableImage(width, height);
            PixelReader reader = source.getPixelReader();
            PixelWriter writer = transparentImage.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    if (isNearlyWhite(color)) {
                        writer.setColor(x, y, Color.TRANSPARENT);
                    } else {
                        writer.setColor(x, y, color);
                    }
                }
            }
            return transparentImage;
        } catch (Exception ex) {
            System.err.println("Failed to remove background from sprite: " + ex.getMessage());
            return source;
        }
    }

    private boolean isNearlyWhite(Color color) {
        return color.getRed() > 0.95 && color.getGreen() > 0.95 && color.getBlue() > 0.95;
    }
}
