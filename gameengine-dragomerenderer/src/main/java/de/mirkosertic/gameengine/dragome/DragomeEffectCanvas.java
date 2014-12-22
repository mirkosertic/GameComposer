package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class DragomeEffectCanvas implements EffectCanvas {

    private final DragomeCanvasRenderingContext2D renderingContext;

    public DragomeEffectCanvas(DragomeCanvasRenderingContext2D renderingContext) {
        this.renderingContext = renderingContext;
    }

    @Override
    public void drawSingleDot(Position aPosition, Color aColor) {
        String theColor = CSSUtils.toColor(aColor);
        renderingContext.setStrokeStyle(theColor);
        renderingContext.strokeRect(aPosition.x, aPosition.y, 1, 1);
    }
}
