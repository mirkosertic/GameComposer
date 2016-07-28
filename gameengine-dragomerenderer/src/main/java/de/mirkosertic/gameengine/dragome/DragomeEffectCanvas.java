package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import org.w3c.dom.html.CanvasRenderingContext2D;

import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import org.w3c.dom.html.HTMLImageElement;

public class DragomeEffectCanvas implements EffectCanvas {

    private final CanvasRenderingContext2D context;

    public DragomeEffectCanvas(CanvasRenderingContext2D aContext) {
        context = aContext;
    }

    @Override
    public void setPaint(Color aColor) {
        String theColor = CSSUtils.toColor(aColor);
        context.setStrokeStyle(theColor);
        context.setFillStyle(theColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        context.strokeRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRect(double aX, double aY, double aWidth, double aHeight) {
        context.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillTriangle(double aX0, double aY0, double aX1, double aY1, double aX2, double aY2) {
        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.fill();
    }

    @Override
    public void fillTriangle(GameResource aTexture, double aX0, double aY0, double aX1, double aY1, double aX2,
            double aY2, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2) {
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
        DragomeGameResource theImage = (DragomeGameResource) aTexture;
        HTMLImageElement theImageSource= (HTMLImageElement) theImage.getElement();
        context.drawImage(theImageSource, 0, 0);

        context.restore();
    }

    @Override
    public void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight) {
        DragomeGameResource theImage = (DragomeGameResource) aResource;
        HTMLImageElement theImageSource= (HTMLImageElement) theImage.getElement();
        context.drawImage(theImageSource, aX, aY, aWidth, aHeight);
    }
}