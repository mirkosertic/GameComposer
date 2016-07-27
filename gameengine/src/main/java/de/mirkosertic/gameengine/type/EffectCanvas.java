package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(double aX, double aY, double aWidth, double aHeight);

    void fillPolygon(double[] aXPositions, double[] aYPositions, int aNumberOfPositions);

    void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight);
}