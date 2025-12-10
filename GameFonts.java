package com.game.mario;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameFonts {
    private static Font spicySaleFont;

    static {
        // Load font dari resource file
        try {
            spicySaleFont = Font.loadFont(
                    GameFonts.class.getResourceAsStream("/fonts/SpicySale.ttf"),
                    20 // Size default
            );
            System.out.println("Font loaded: " + (spicySaleFont != null));
        } catch (Exception e) {
            System.err.println("Failed to load font: " + e.getMessage());
            // Fallback ke font system
            spicySaleFont = Font.font("Fredoka One", FontWeight.BOLD, 20);
        }
    }

    // === METHOD YANG DIPERLUKAN ===

    public static Font bold(int size) {
        if (spicySaleFont != null) {
            return Font.font(spicySaleFont.getFamily(), FontWeight.BOLD, size);
        } else {
            return Font.font("Fredoka One", FontWeight.BOLD, size);
        }
    }

    public static Font of(int size) {
        return bold(size); // Alias untuk bold
    }

    public static Font semiBold(int size) {
        if (spicySaleFont != null) {
            return Font.font(spicySaleFont.getFamily(), FontWeight.SEMI_BOLD, size);
        } else {
            return Font.font("Fredoka One", FontWeight.SEMI_BOLD, size);
        }
    }

    public static Font semiBold(double size) {
        return semiBold((int) size); // Convert double ke int
    }

    public static Font extraBold(int size) {
        if (spicySaleFont != null) {
            return Font.font(spicySaleFont.getFamily(), FontWeight.EXTRA_BOLD, size);
        } else {
            return Font.font("Fredoka One", FontWeight.EXTRA_BOLD, size);
        }
    }

    public static Font regular(int size) {
        if (spicySaleFont != null) {
            return Font.font(spicySaleFont.getFamily(), FontWeight.NORMAL, size);
        } else {
            return Font.font("Fredoka One", FontWeight.NORMAL, size);
        }
    }

    // Method getFamily() untuk compatibility
    public static String getFamily() {
        if (spicySaleFont != null) {
            return spicySaleFont.getFamily();
        } else {
            return "Fredoka One";
        }
    }

    // Method initialize() jika diperlukan
    public static void initialize() {
        // Already initialized in static block
    }
}