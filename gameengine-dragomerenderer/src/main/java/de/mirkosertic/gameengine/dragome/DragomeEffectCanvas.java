package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import org.w3c.dom.html.CanvasRenderingContext2D;

import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import org.w3c.dom.html.HTMLImageElement;

public class DragomeEffectCanvas implements EffectCanvas {

    private final CanvasRenderingContext2D renderingContext;

    public DragomeEffectCanvas(CanvasRenderingContext2D renderingContext) {
        this.renderingContext = renderingContext;
    }

    @Override
    public void setPaint(Color aColor) {
        String theColor = CSSUtils.toColor(aColor);
        renderingContext.setStrokeStyle(theColor);
        renderingContext.setFillStyle(theColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        renderingContext.strokeRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRect(float aX, float aY, float aWidth, float aHeight) {
        renderingContext.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions) {
        renderingContext.beginPath();
        renderingContext.moveTo(aXPositions[0], aYPositions[0]);
        for (int i=1;i<aNumberOfPositions;i++) {
            renderingContext.lineTo(aXPositions[i], aYPositions[i]);
        }
        renderingContext.closePath();
        renderingContext.fill();
    }

    @Override
    public void drawScaled(GameResource aResource, float aX, float aY, float aWidth, float aHeight) {
        DragomeGameResource theImage = (DragomeGameResource) aResource;
        HTMLImageElement theImageSource= (HTMLImageElement) theImage.getElement();
        renderingContext.drawImage(theImageSource, aX, aY, aWidth, aWidth);
    }
}