package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void drawSingleDot(String aObjectIdentifier, Position aPosition, Color aColor, int aZIndex);

    void fillRect(String aObjectIdentifier, int aX, int aY, int aWidth, int aHeight, Color aColor, int aZIndex);

    void fillTriangle(String aObjectIdentifier, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, Color aColor, int aZIndex);

    void fillTriangle(String aObjectID, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2, int aZIndex);

    void drawScaled(String aObjectID, GameResource aResource, int aX, int aY, int aWidth, int aHeight, int aZIndex);
}