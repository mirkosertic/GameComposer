package de.mirkosertic.gameengine.arcaderacer;

public class Road {

    private final float positionLeft;
    private final float positionRight;
    private final float curbWidth;
    private final int curveFactor;

    public Road(float aPositionLeft, float aPositionRight, float aCurbWidth, int aCurveFactor) {
        positionLeft = aPositionLeft;
        positionRight = aPositionRight;
        curbWidth = aCurbWidth;
        curveFactor = aCurveFactor;
    }

    public float getPositionLeft() {
        return positionLeft;
    }

    public float getPositionRight() {
        return positionRight;
    }

    public float getCurbWidth() {
        return curbWidth;
    }

    public int getCurveFactor() {
        return curveFactor;
    }
}
