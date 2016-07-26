package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

import org.teavm.jso.canvas.CanvasRenderingContext2D;

public class TeaVMEffectCanvas implements EffectCanvas {

    private final CanvasRenderingContext2D context;

    public TeaVMEffectCanvas(CanvasRenderingContext2D context) {
        this.context = context;
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
    public void fillRect(float aX, float aY, float aWidth, float aHeight) {
        context.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions) {
        context.beginPath();
        context.moveTo(aXPositions[0], aYPositions[0]);
        for (int i=1;i<aNumberOfPositions;i++) {
            context.lineTo(aXPositions[i], aYPositions[i]);
        }
        context.closePath();
        context.fill();
    }

    @Override
    public void drawScaled(GameResource aResource, float aX, float aY, float aWidth, float aHeight) {
    }
}