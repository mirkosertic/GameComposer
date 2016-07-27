package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;

public class Road {

    public final double positionLeft;
    public final double positionRight;
    public final int curveFactor;
    public final Color roadColor;
    public final Curb[] curbs;

    public Road(double aPositionLeft, double aPositionRight, int aCurveFactor, Color aRoadColor, Curb[] aCurbs) {
        positionLeft = aPositionLeft;
        positionRight = aPositionRight;
        curbs = aCurbs;
        curveFactor = aCurveFactor;
        roadColor = aRoadColor;
    }
}