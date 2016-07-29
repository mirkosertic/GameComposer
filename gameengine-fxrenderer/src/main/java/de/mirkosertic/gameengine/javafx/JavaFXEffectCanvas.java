package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import javafx.scene.canvas.GraphicsContext;

public class JavaFXEffectCanvas implements EffectCanvas {

    private final GraphicsContext context;

    public JavaFXEffectCanvas(GraphicsContext aContext) {
        context = aContext;
    }

    @Override
    public void setPaint(Color aColor) {
        javafx.scene.paint.Color theFXColor = javafx.scene.paint.Color.rgb(aColor.r, aColor.g, aColor.b);
        context.setFill(theFXColor);
        context.setStroke(theFXColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        context.fillRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRect(int aX, int aY, int aWidth, int aHeight) {
        context.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillTriangle(int aX0, int aY0, int aX1, int aY1, int aX2, int aY2) {
        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.stroke();
        context.fill();
    }

    @Override
    public void fillTriangle(GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2,
            int aY2, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2) {

        // Affine Texture Mapping
        context.save();

        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.clip();

        aX1 -= aX0; aY1 -= aY0; aX2 -= aX0; aY2 -= aY0;
        aU1 -= aU0; aV1 -= aV0; aU2 -= aU0; aV2 -= aV0;

        double id = 1.0 / (aU1*aV2 - aU2*aV1);
        double a = id * (aV2*aX1 - aV1*aX2);
        double b = id * (aV2*aY1 - aV1*aY2);
        double c = id * (aU1*aX2 - aU2*aX1);
        double d = id * (aU1*aY2 - aU2*aY1);
        double e = aX0 - a*aU0 - c*aV0;
        double f = aY0 - b*aU0 - d*aV0;

        context.transform( a, b, c, d, e, f );
        JavaFXBitmapResource theBitmap = (JavaFXBitmapResource) aTexture;
        context.drawImage(theBitmap, 0, 0);

        context.restore();
    }

    @Override
    public void drawScaled(GameResource aResource, int aX, int aY, int aWidth, int aHeight) {
        JavaFXBitmapResource theBitmap = (JavaFXBitmapResource) aResource;
        context.drawImage(theBitmap, aX, aY, aWidth, aHeight);
    }
}