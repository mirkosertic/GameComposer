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

    @Override
    public void setPaint(Color aColor) {
        currentPaint = AndroidUtils.toFilledPaint(aColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        canvas.drawRect(aPosition.x, aPosition.y, aPosition.x + 1, aPosition.y + 1, currentPaint);
    }

    @Override
    public void fillRect(float aX, float aY, float aWidth, float aHeight) {
        canvas.drawRect(aX, aY, aX + aWidth, aY + aHeight, currentPaint);
    }

    @Override
    public void fillPolygon(float[] aXPositions, float[] aYPositions, int aNumberOfPositions) {
        Path thePath = new Path();
        thePath.moveTo(aXPositions[0], aYPositions[0]);
        for (int i=0;i<aNumberOfPositions;i++) {
            thePath.lineTo(aXPositions[i], aYPositions[i]);
        }
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }

    @Override
    public void drawScaled(GameResource aResource, float aX, float aY, float aWidth, float aHeight) {
    }
}