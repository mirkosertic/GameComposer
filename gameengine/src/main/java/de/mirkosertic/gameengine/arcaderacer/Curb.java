package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;

public class Curb {

    private final double position;
    private final double width;
    private final Color color;

    public Curb(double aPosition, double aWidth, Color aColor) {
        position = aPosition;
        width = aWidth;
        color = aColor;
    }

    public double getPosition() {
        return position;
    }

    public double getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }
}