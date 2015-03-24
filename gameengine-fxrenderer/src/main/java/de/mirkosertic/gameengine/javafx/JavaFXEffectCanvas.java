package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

import javafx.scene.canvas.GraphicsContext;

public class JavaFXEffectCanvas implements EffectCanvas {

    private final GraphicsContext context;

    public JavaFXEffectCanvas(GraphicsContext context) {
        this.context = context;
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
}