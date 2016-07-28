package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Road {

    public final double positionLeft;
    public final double positionRight;
    public final int curveFactor;
    public final Color roadColor;
    public final Curb[] curbs;
    public final ResourceName texture;

    public Road(ResourceName aTexture, double aPositionLeft, double aPositionRight, int aCurveFactor, Color aRoadColor, Curb[] aCurbs) {
        positionLeft = aPositionLeft;
        positionRight = aPositionRight;
        curbs = aCurbs;
        curveFactor = aCurveFactor;
        roadColor = aRoadColor;
        texture = aTexture;
    }
}