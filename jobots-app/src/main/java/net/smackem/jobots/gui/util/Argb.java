package net.smackem.jobots.gui.util;

import javafx.scene.paint.Color;

public class Argb {
    private Argb() {}

    public static Color toColor(int argb) {
        return Color.rgb(
                argb >> 16 & 255,
                argb >> 8 & 255,
                argb & 255,
                (argb >> 24 & 255) / 255.0);
    }

    public static int fromColor(Color color) {
        return ((int) (color.getOpacity() * 255) << 24) |
               ((int) (color.getRed() * 255) << 16) |
               ((int) (color.getGreen() * 255) << 8) |
               ((int) (color.getBlue() * 255));
    }
}
