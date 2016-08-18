package de.mirkosertic.gameengine.arcaderacer;

public class TrackElement {

    public final int curveFactor;
    public final Segment[] segments;
    public final double height;
    public final Sprite[] sprites;
    public final int angle;

    public TrackElement(int aCurveFactor, Segment[] aSegments, double aHeight, Sprite[] aSprites, int aAngle) {
        curveFactor = aCurveFactor;
        segments = aSegments;
        height = aHeight;
        sprites = aSprites;
        angle = aAngle;
    }
}