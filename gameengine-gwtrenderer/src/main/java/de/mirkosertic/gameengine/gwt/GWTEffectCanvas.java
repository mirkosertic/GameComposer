package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.dom.client.Context2d;

import com.google.gwt.canvas.dom.client.CssColor;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class GWTEffectCanvas implements EffectCanvas {

    private final Context2d context;

    public GWTEffectCanvas(Context2d context) {
        this.context = context;
    }

    @Override
    public void drawSingleDot(Position aPosition, Color aColor) {
        CssColor theColor = CssColor.make(aColor.r, aColor.g, aColor.b);
        context.setFillStyle(theColor);
        context.setStrokeStyle(theColor);
        context.fillRect(aPosition.x, aPosition.y, 1, 1);
    }
}