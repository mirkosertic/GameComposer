package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;

public class AndroidEffectCanvas implements EffectCanvas {

    private final Canvas canvas;
    private Paint currentPaint;

    public AndroidEffectCanvas(Canvas aCanvas) {
        canvas = aCanvas;
    }

    private void updatePaint(Color aColor) {
        currentPaint = AndroidUtils.toFilledPaint(aColor);
    }

    @Override
    public void drawSingleDot(String aObjectID, Position aPosition, Color aColor, int aZIndex) {
        updatePaint(aColor);
        canvas.drawRect(aPosition.x, aPosition.y, aPosition.x + 1, aPosition.y + 1, currentPaint);
    }

    @Override
    public void fillRectangle(String aObjectID, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2,int aX3, int aY3, Color aColor, int aZIndex) {
        updatePaint(aColor);
        Path thePath = new Path();
        thePath.moveTo((float) aX0, (float) aY0);
        thePath.lineTo((float) aX1, (float) aY1);
        thePath.lineTo((float) aX2, (float) aY2);
        thePath.lineTo((float) aX3, (float) aY3);
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }

    @Override
    public void fillRectangle(String aObjectID, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2,
                              int aY2, int aX3, int aY3, int aU0, int aV0, int aU1, int aV1, int aU2, int aV2, int aU3, int aV3, int aZIndex) {
        Path thePath = new Path();
        thePath.moveTo((float) aX0, (float) aY0);
        thePath.lineTo((float) aX1, (float) aY1);
        thePath.lineTo((float) aX2, (float) aY2);
        thePath.lineTo((float) aX3, (float) aY3);
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }
}