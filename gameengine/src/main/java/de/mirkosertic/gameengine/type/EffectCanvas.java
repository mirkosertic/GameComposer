package de.mirkosertic.gameengine.type;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(int aX, int aY, int aWidth, int aHeight);

    void fillPolygon(int[] aXPositions, int[] aYPositions, int aNumberOfPositions);
}