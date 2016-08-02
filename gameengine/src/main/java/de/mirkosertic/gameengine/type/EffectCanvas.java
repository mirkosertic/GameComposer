package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void drawSingleDot(String aObjectIdentifier, Position aPosition, Color aColor, int aZIndex);

    void fillRectangle(String aObjectIdentifier, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, Color aColor, int aZIndex);

    void fillRectangle(String aObjectIdentifier, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2, int aU3, int aV3, int aZIndex);
}