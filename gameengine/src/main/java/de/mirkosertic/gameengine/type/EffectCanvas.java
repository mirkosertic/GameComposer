package de.mirkosertic.gameengine.type;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(float aX, float aY, float aWidth, float aHeight);

    void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions);

    void drawLine(float aX1, float aY1, float aX2, float aY2);
}