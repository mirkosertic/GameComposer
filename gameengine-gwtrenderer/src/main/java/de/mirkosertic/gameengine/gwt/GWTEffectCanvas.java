package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class GWTEffectCanvas implements EffectCanvas {

    private final Context2d context;

    public GWTEffectCanvas(Context2d context) {
        this.context = context;
    }

    @Override
    public void setPaint(Color aColor) {
        CssColor theColor = CssColor.make(aColor.r, aColor.g, aColor.b);
        context.setFillStyle(theColor);
        context.setStrokeStyle(theColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        context.fillRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRect(double aX, double aY, double aWidth, double aHeight) {
        context.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillPolygon(double[] aXPositions, double[] aYPositions, int aNumberOfPositions) {
        context.beginPath();
        context.moveTo(aXPositions[0], aYPositions[0]);
        for (int i=1;i<aNumberOfPositions;i++) {
            context.lineTo(aXPositions[i], aYPositions[i]);
        }
        context.closePath();
        context.fill();
    }

    @Override
    public void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight) {
        GWTBitmapResource theImage = (GWTBitmapResource) aResource;
        context.drawImage(theImage.getImage(), aX, aY, aWidth, aHeight);
    }
}