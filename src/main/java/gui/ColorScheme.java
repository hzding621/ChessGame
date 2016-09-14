package gui;

import javafx.scene.paint.Color;

/**
 * Global static definition of color schemes used in chess board
 */
public enum ColorScheme {

    STANDARD(Color.rgb(0xFF, 0xCE, 0x9E), Color.rgb(0xD1, 0x8B, 0x47));

    private final Color light;
    private final Color dark;

    ColorScheme(Color dark, Color light) {
        this.dark = dark;
        this.light = light;
    }

    public Color light() {
        return light;
    }

    public Color dark() {
        return dark;
    }

}
