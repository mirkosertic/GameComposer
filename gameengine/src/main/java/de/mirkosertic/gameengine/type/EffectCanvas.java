package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(float aX, float aY, float aWidth, float aHeight);

    void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions);

    void drawScaled(GameResource aResource, float aX, float aY, float aWidth, float aHeight);
}