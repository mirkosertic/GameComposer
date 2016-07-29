package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(int aX, int aY, int aWidth, int aHeight);

    void fillTriangle(int aX0, int aY0, int aX1, int aY1, int aX2, int aY2);

    void fillTriangle(GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2);

    void drawScaled(GameResource aResource, int aX, int aY, int aWidth, int aHeight);
}