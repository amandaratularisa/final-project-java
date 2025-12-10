package com.game.mario;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class PixelText extends StackPane {
    private final int pixelSize;
    private final PixelFontRenderer.Palette palette;
    private final int letterSpacing;
    private final int lineSpacing;
    private Canvas canvas;
    private String currentText;

    public PixelText(String text, int pixelSize, PixelFontRenderer.Palette palette) {
        this(text, pixelSize, palette, -1, -1);
    }

    public PixelText(String text, int pixelSize, PixelFontRenderer.Palette palette, int letterSpacing, int lineSpacing) {
        this.pixelSize = pixelSize;
        this.palette = palette;
        this.letterSpacing = letterSpacing <= 0 ? pixelSize / 2 : letterSpacing;
        this.lineSpacing = lineSpacing <= 0 ? pixelSize : lineSpacing;
        setText(text);
    }

    public void setText(String text) {
        this.currentText = text;
        Canvas newCanvas = PixelFontRenderer.createTextCanvas(text, pixelSize, palette, letterSpacing, lineSpacing);
        getChildren().clear();
        getChildren().add(newCanvas);
        this.canvas = newCanvas;
    }

    public String getText() {
        return currentText;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
