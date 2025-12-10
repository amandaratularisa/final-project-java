package com.game.mario;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

public class CustomButton {
    private double x, y, width, height;
    private String text;
    private Color bgColor, hoverColor, textColor;
    private boolean hovered = false;
    private boolean pressed = false; // <- DIPINDAH KE SINI
    private boolean enabled = true;
    private Runnable onClick;

    public CustomButton(double x, double y, double width, double height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.bgColor = Color.rgb(70, 130, 220);
        this.hoverColor = Color.rgb(90, 150, 240);
        this.textColor = Color.WHITE;
    }

    public CustomButton(double x, double y, double width, double height, String text, Color bgColor) {
        this(x, y, width, height, text);
        this.bgColor = bgColor;
        this.hoverColor = bgColor.brighter();
    }

    public void setOnClick(Runnable action) {
        this.onClick = action;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean contains(double mouseX, double mouseY) {
        return enabled && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void checkHover(double mouseX, double mouseY) {
        hovered = contains(mouseX, mouseY);
    }

    // === METHOD handleClick YANG BARU & TEPAT ===
    public void handleClick(double mouseX, double mouseY) {
        if (enabled && hovered && onClick != null) {
            pressed = true;
            // Reset pressed state setelah delay
            new Thread(() -> {
                try {
                    Thread.sleep(150);
                    pressed = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            onClick.run();
        }
    }

    public void draw(GraphicsContext gc) {
        Color currentColor = hovered ? hoverColor : bgColor;

        if (!enabled) {
            currentColor = Color.rgb(100, 100, 100);
        }

        double offsetX = pressed ? 2 : 0;
        double offsetY = pressed ? 2 : 0;

        // === GLOW SHADOW EFFECT ===
        if (hovered && !pressed) {
            // Glow effect saat hover
            gc.setFill(Color.rgb(
                    (int) (currentColor.getRed() * 255),
                    (int) (currentColor.getGreen() * 255),
                    (int) (currentColor.getBlue() * 255),
                    0.3));
            gc.fillRoundRect(x - 3 + offsetX, y - 3 + offsetY, width + 6, height + 6, 20, 20);
        }

        // === REGULAR SHADOW ===
        if (!pressed) {
            gc.setFill(Color.rgb(0, 0, 0, 0.25));
            gc.fillRoundRect(x + 4 + offsetX, y + 4 + offsetY, width, height, 15, 15);
        }

        // === BODY BUTTON ===
        LinearGradient gradient = new LinearGradient(0, y + offsetY, 0, y + height + offsetY, false,
                CycleMethod.NO_CYCLE,
                new Stop(0, currentColor.brighter().brighter()),
                new Stop(0.5, currentColor),
                new Stop(1, currentColor.darker().darker()));
        gc.setFill(gradient);
        gc.fillRoundRect(x + offsetX, y + offsetY, width, height, 15, 15);

        // === TEKS ===
        gc.setFont(GameFonts.bold(18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillText(text, x + width / 2 + 1 + offsetX, y + height / 2 + 1 + offsetY);

        gc.setFill(textColor);
        gc.fillText(text, x + width / 2 + offsetX, y + height / 2 + offsetY);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isHovered() {
        return hovered;
    }
}