package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.core.GameResource;

public interface EffectCanvas {

    void setPaint(Color aColor);

    void drawSingleDot(Position aPosition);

    void fillRect(double aX, double aY, double aWidth, double aHeight);

    void fillTriangle(double aX0, double aY0, double aX1, double aY1, double aX2, double aY2);

    void fillTriangle(GameResource aTexture, double aX0, double aY0, double aX1, double aY1, double aX2, double aY2, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2);

    void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight);
}