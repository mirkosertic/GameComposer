package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Segment {

    public final double xStart;
    public final double yStart;
    public final double xEnd;
    public final double yEnd;
    public final ResourceName texture;
    public final Color color;

    public Segment(double aXStart, double aYStart, double aXEnd, double aYEnd, ResourceName aTexture) {
        xStart = aXStart;
        yStart = aYStart;
        xEnd = aXEnd;
        yEnd = aYEnd;
        texture = aTexture;
        color = null;
    }

    public Segment(double aXStart, double aYStart, double aXEnd, double aYEnd, Color aColor) {
        xStart = aXStart;
        yStart = aYStart;
        xEnd = aXEnd;
        yEnd = aYEnd;
        texture = null;
        color = aColor;
    }
}