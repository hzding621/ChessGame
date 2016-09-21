package gui;

import javafx.scene.paint.Color;

/**
 * Global static definition of color schemes used in chess board
 */
public enum ColorScheme {

    STANDARD(Color.rgb(0xFF, 0xCE, 0x9E), Color.rgb(0xD1, 0x8B, 0x47), Color.YELLOW,
            Color.rgb(0xAD, 0xCC, 0x85), Color.rgb(0x9F, 0xB4, 0x62), Color.RED);

    private final Color light;
    private final Color dark;
    private final Color highlighted;
    private final Color movableLight;
    private final Color movableDark;
    private final Color attacked;

    ColorScheme(Color dark, Color light, Color highlighted, Color movableLight, Color movableDark, Color attacked) {
        this.dark = dark;
        this.light = light;
        this.highlighted = highlighted;
        this.movableLight = movableLight;
        this.movableDark = movableDark;
        this.attacked = attacked;
    }

    public Color light() {
        return light;
    }

    public Color dark() {
        return dark;
    }


    public Color highlighted() {
        return highlighted;
    }

    public Color movableLight() {
        return movableLight;
    }

    public Color movableDark() {
        return movableDark;
    }

    public Color getAttacked() {
        return attacked;
    }
}
