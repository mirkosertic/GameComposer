package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class AndroidEffectCanvas implements EffectCanvas {

    private final Canvas canvas;

    public AndroidEffectCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void drawSingleDot(Position aPosition, Color aColor) {
        canvas.drawRect(aPosition.x, aPosition.y, aPosition.x + 1, aPosition.y + 1,
                AndroidUtils.toFilledPaint(aColor));
    }
}