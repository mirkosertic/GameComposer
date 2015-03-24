package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Paint;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class AndroidEffectCanvas implements EffectCanvas {

    private final Canvas canvas;

    private Paint currentPaint;

    public AndroidEffectCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void setPaint(Color aColor) {
        currentPaint = AndroidUtils.toFilledPaint(aColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        canvas.drawRect(aPosition.x, aPosition.y, aPosition.x + 1, aPosition.y + 1, currentPaint);
    }
}