package de.mirkosertic.gameengine.teavm;

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
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        context.strokeRect(aPosition.x, aPosition.y, 1, 1);
    }
}