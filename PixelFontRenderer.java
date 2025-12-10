package com.game.mario;

import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class PixelFontRenderer {
    private static final int CHAR_HEIGHT = 7;
    private static final Map<Character, String[]> FONT = createFont();

    public static class Palette {
        public final Color fill;
        public final Color outline;
        public final Color shadow;

        public Palette(Color fill, Color outline, Color shadow) {
            this.fill = fill;
            this.outline = outline;
            this.shadow = shadow;
        }
    }

    public static final Palette TITLE = new Palette(Color.WHITE, Color.web("#f6f763"), Color.web("#1a1a40"));
    public static final Palette PRIMARY = new Palette(Color.web("#e2f1ff"), Color.web("#7bdff2"), Color.web("#0b1c33"));
    public static final Palette ACCENT = new Palette(Color.web("#ffe3a0"), Color.web("#ffb347"), Color.web("#421f00"));
    public static final Palette ALERT = new Palette(Color.web("#ffb2b2"), Color.web("#ff2965"), Color.web("#2b0010"));
    public static final Palette SUCCESS = new Palette(Color.web("#d8ffd8"), Color.web("#8bff8b"), Color.web("#002b11"));

    private PixelFontRenderer() {}

    public static StackPane createTextNode(String text, int pixelSize, Palette palette) {
        Canvas canvas = createTextCanvas(text, pixelSize, palette, getDefaultLetterSpacing(pixelSize), getDefaultLineSpacing(pixelSize));
        StackPane pane = new StackPane(canvas);
        pane.setPadding(new Insets(2));
        return pane;
    }

    public static Canvas createTextCanvas(String text, int pixelSize, Palette palette, int letterSpacing, int lineSpacing) {
        double width = measureWidth(text, pixelSize, letterSpacing);
        double height = measureHeight(text, pixelSize, lineSpacing);
        Canvas canvas = new Canvas(width, height);
        render(canvas.getGraphicsContext2D(), text, 0, 0, pixelSize, palette, letterSpacing, lineSpacing);
        return canvas;
    }

    public static Image createTextImage(String text, int pixelSize, Palette palette, int letterSpacing, int lineSpacing) {
        Canvas canvas = createTextCanvas(text, pixelSize, palette, letterSpacing, lineSpacing);
        WritableImage image = new WritableImage((int)Math.ceil(canvas.getWidth()), (int)Math.ceil(canvas.getHeight()));
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return canvas.snapshot(params, image);
    }

    public static void drawText(GraphicsContext gc, String text, double x, double y, int pixelSize, Palette palette) {
        drawText(gc, text, x, y, pixelSize, palette, getDefaultLetterSpacing(pixelSize), getDefaultLineSpacing(pixelSize));
    }

    public static void drawText(GraphicsContext gc, String text, double x, double y, int pixelSize, Palette palette,
                                int letterSpacing, int lineSpacing) {
        render(gc, text, x, y, pixelSize, palette, letterSpacing, lineSpacing);
    }

    private static void render(GraphicsContext gc, String text, double startX, double startY, int pixelSize,
                               Palette palette, int letterSpacing, int lineSpacing) {
        double x = startX;
        double y = startY;
        int outlineSize = Math.max(1, pixelSize / 4);
        int shadowOffset = Math.max(2, pixelSize / 2);
        String upper = text.toUpperCase();
        for (int i = 0; i < upper.length(); i++) {
            char ch = upper.charAt(i);
            if (ch == '\n') {
                y += CHAR_HEIGHT * pixelSize + lineSpacing;
                x = startX;
                continue;
            }
            if (ch == ' ') {
                x += pixelSize * 2;
                continue;
            }
            String[] pattern = FONT.getOrDefault(ch, FONT.get('?'));
            drawCharacter(gc, pattern, x, y, pixelSize, palette, outlineSize, shadowOffset);
            x += pattern[0].length() * pixelSize + letterSpacing;
        }
    }

    private static void drawCharacter(GraphicsContext gc, String[] pattern, double offsetX, double offsetY, int pixelSize,
                                      Palette palette, int outlineSize, int shadowOffset) {
        for (int row = 0; row < pattern.length; row++) {
            String line = pattern[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '1') {
                    double px = offsetX + col * pixelSize;
                    double py = offsetY + row * pixelSize;
                    gc.setFill(palette.shadow);
                    gc.fillRect(px + shadowOffset, py + shadowOffset, pixelSize, pixelSize);
                    gc.setFill(palette.outline);
                    gc.fillRect(px - outlineSize / 2.0, py - outlineSize / 2.0, pixelSize + outlineSize, pixelSize + outlineSize);
                    gc.setFill(palette.fill);
                    gc.fillRect(px, py, pixelSize, pixelSize);
                }
            }
        }
    }

    public static double measureWidth(String text, int pixelSize, int letterSpacing) {
        double width = 0;
        double maxWidth = 0;
        String upper = text.toUpperCase();
        for (int i = 0; i < upper.length(); i++) {
            char ch = upper.charAt(i);
            if (ch == '\n') {
                maxWidth = Math.max(maxWidth, width);
                width = 0;
                continue;
            }
            if (ch == ' ') {
                width += pixelSize * 2;
                continue;
            }
            String[] pattern = FONT.getOrDefault(ch, FONT.get('?'));
            width += pattern[0].length() * pixelSize + letterSpacing;
        }
        if (width > 0) {
            width -= letterSpacing;
        }
        return Math.max(maxWidth, width);
    }

    public static double measureHeight(String text, int pixelSize, int lineSpacing) {
        int lines = 1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') lines++;
        }
        return lines * CHAR_HEIGHT * pixelSize + (lines - 1) * lineSpacing;
    }

    private static int getDefaultLetterSpacing(int pixelSize) {
        return Math.max(2, pixelSize / 2);
    }

    private static int getDefaultLineSpacing(int pixelSize) {
        return Math.max(pixelSize, 6);
    }

    private static Map<Character, String[]> createFont() {
        Map<Character, String[]> font = new HashMap<>();
        font.put('A', new String[]{
            "01110",
            "10001",
            "10001",
            "11111",
            "10001",
            "10001",
            "10001"
        });
        font.put('B', new String[]{
            "11110",
            "10001",
            "10001",
            "11110",
            "10001",
            "10001",
            "11110"
        });
        font.put('C', new String[]{
            "01110",
            "10001",
            "10000",
            "10000",
            "10000",
            "10001",
            "01110"
        });
        font.put('D', new String[]{
            "11100",
            "10010",
            "10001",
            "10001",
            "10001",
            "10010",
            "11100"
        });
        font.put('E', new String[]{
            "11111",
            "10000",
            "10000",
            "11110",
            "10000",
            "10000",
            "11111"
        });
        font.put('F', new String[]{
            "11111",
            "10000",
            "10000",
            "11110",
            "10000",
            "10000",
            "10000"
        });
        font.put('G', new String[]{
            "01110",
            "10001",
            "10000",
            "10011",
            "10001",
            "10001",
            "01110"
        });
        font.put('H', new String[]{
            "10001",
            "10001",
            "10001",
            "11111",
            "10001",
            "10001",
            "10001"
        });
        font.put('I', new String[]{
            "11111",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100",
            "11111"
        });
        font.put('J', new String[]{
            "00111",
            "00010",
            "00010",
            "00010",
            "10010",
            "10010",
            "01100"
        });
        font.put('K', new String[]{
            "10001",
            "10010",
            "10100",
            "11000",
            "10100",
            "10010",
            "10001"
        });
        font.put('L', new String[]{
            "10000",
            "10000",
            "10000",
            "10000",
            "10000",
            "10000",
            "11111"
        });
        font.put('M', new String[]{
            "10001",
            "11011",
            "10101",
            "10101",
            "10001",
            "10001",
            "10001"
        });
        font.put('N', new String[]{
            "10001",
            "11001",
            "10101",
            "10011",
            "10001",
            "10001",
            "10001"
        });
        font.put('O', new String[]{
            "01110",
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01110"
        });
        font.put('P', new String[]{
            "11110",
            "10001",
            "10001",
            "11110",
            "10000",
            "10000",
            "10000"
        });
        font.put('Q', new String[]{
            "01110",
            "10001",
            "10001",
            "10001",
            "10101",
            "10010",
            "01101"
        });
        font.put('R', new String[]{
            "11110",
            "10001",
            "10001",
            "11110",
            "10100",
            "10010",
            "10001"
        });
        font.put('S', new String[]{
            "01111",
            "10000",
            "10000",
            "01110",
            "00001",
            "00001",
            "11110"
        });
        font.put('T', new String[]{
            "11111",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100",
            "00100"
        });
        font.put('U', new String[]{
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01110"
        });
        font.put('V', new String[]{
            "10001",
            "10001",
            "10001",
            "10001",
            "10001",
            "01010",
            "00100"
        });
        font.put('W', new String[]{
            "10001",
            "10001",
            "10001",
            "10101",
            "10101",
            "10101",
            "01010"
        });
        font.put('X', new String[]{
            "10001",
            "10001",
            "01010",
            "00100",
            "01010",
            "10001",
            "10001"
        });
        font.put('Y', new String[]{
            "10001",
            "10001",
            "01010",
            "00100",
            "00100",
            "00100",
            "00100"
        });
        font.put('Z', new String[]{
            "11111",
            "00001",
            "00010",
            "00100",
            "01000",
            "10000",
            "11111"
        });
        font.put('0', new String[]{
            "01110",
            "10001",
            "10011",
            "10101",
            "11001",
            "10001",
            "01110"
        });
        font.put('1', new String[]{
            "00100",
            "01100",
            "00100",
            "00100",
            "00100",
            "00100",
            "01110"
        });
        font.put('2', new String[]{
            "01110",
            "10001",
            "00001",
            "00010",
            "00100",
            "01000",
            "11111"
        });
        font.put('3', new String[]{
            "11110",
            "00001",
            "00001",
            "00110",
            "00001",
            "00001",
            "11110"
        });
        font.put('4', new String[]{
            "10010",
            "10010",
            "10010",
            "11111",
            "00010",
            "00010",
            "00010"
        });
        font.put('5', new String[]{
            "11111",
            "10000",
            "10000",
            "11110",
            "00001",
            "00001",
            "11110"
        });
        font.put('6', new String[]{
            "01110",
            "10000",
            "10000",
            "11110",
            "10001",
            "10001",
            "01110"
        });
        font.put('7', new String[]{
            "11111",
            "00001",
            "00010",
            "00100",
            "01000",
            "01000",
            "01000"
        });
        font.put('8', new String[]{
            "01110",
            "10001",
            "10001",
            "01110",
            "10001",
            "10001",
            "01110"
        });
        font.put('9', new String[]{
            "01110",
            "10001",
            "10001",
            "01111",
            "00001",
            "00001",
            "01110"
        });
        font.put('!', new String[]{
            "00100",
            "00100",
            "00100",
            "00100",
            "00100",
            "00000",
            "00100"
        });
        font.put('?', new String[]{
            "01110",
            "10001",
            "00010",
            "00100",
            "00100",
            "00000",
            "00100"
        });
        font.put(':', new String[]{
            "000",
            "010",
            "000",
            "000",
            "000",
            "010",
            "000"
        });
        font.put('.', new String[]{
            "0",
            "0",
            "0",
            "0",
            "0",
            "0",
            "1"
        });
        font.put(',', new String[]{
            "0",
            "0",
            "0",
            "0",
            "0",
            "1",
            "1"
        });
        font.put('-', new String[]{
            "0000",
            "0000",
            "0000",
            "1111",
            "0000",
            "0000",
            "0000"
        });
        font.put('"', new String[]{
            "101",
            "101",
            "101",
            "000",
            "000",
            "000",
            "000"
        });
        font.put('(', new String[]{
            "0011",
            "0100",
            "1000",
            "1000",
            "1000",
            "0100",
            "0011"
        });
        font.put(')', new String[]{
            "1100",
            "0010",
            "0001",
            "0001",
            "0001",
            "0010",
            "1100"
        });
        font.put('/', new String[]{
            "00001",
            "00010",
            "00100",
            "01000",
            "10000",
            "00000",
            "00000"
        });
        font.put('"', font.get('"'));
        font.put('\'', new String[]{
            "1",
            "1",
            "1",
            "0",
            "0",
            "0",
            "0"
        });
        font.put('#', new String[]{
            "01010",
            "11111",
            "01010",
            "11111",
            "01010",
            "01010",
            "01010"
        });
        font.put('%', new String[]{
            "11001",
            "11010",
            "00100",
            "01000",
            "10110",
            "00110",
            "00000"
        });
        font.put('=', new String[]{
            "00000",
            "00000",
            "11111",
            "00000",
            "11111",
            "00000",
            "00000"
        });
        return font;
    }
}
