package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void drawSingleDot(String aObjectIdentifier, Position aPosition, Color aColor, int aZIndex);

    void fillRectangle(String aObjectIdentifier, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, Color aColor, int aZIndex);

    void fillRectangle(String aObjectIdentifier, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2, double aU3, double aV3, int aZIndex);
}