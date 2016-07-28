package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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
    public void fillRect(double aX, double aY, double aWidth, double aHeight) {
        canvas.drawRect((float) aX, (float)aY, (float)(aX + aWidth), (float)(aY + aHeight), currentPaint);
    }

    @Override
    public void fillTriangle(double aX0, double aY0, double aX1, double aY1, double aX2, double aY2) {
        Path thePath = new Path();
        thePath.moveTo((float) aX0, (float) aY0);
        thePath.lineTo((float) aX1, (float) aY1);
        thePath.lineTo((float) aX2, (float) aY2);
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }

    @Override
    public void fillTriangle(GameResource aTexture, double aX0, double aY0, double aX1, double aY1, double aX2,
            double aY2, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2) {
        Path thePath = new Path();
        thePath.moveTo((float) aX0, (float) aY0);
        thePath.lineTo((float) aX1, (float) aY1);
        thePath.lineTo((float) aX2, (float) aY2);
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }

    @Override
    public void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight) {
        AndroidBitmapResource theResource = (AndroidBitmapResource) aResource;
        Rect theSource = new Rect(0, 0, theResource.bitmap.getWidth(), theResource.bitmap.getHeight());
        Rect theDestination = new Rect((int) aX, (int) aY, (int) (aX + aWidth), (int) (aY + aHeight));
        canvas.drawBitmap(theResource.bitmap, theSource, theDestination, currentPaint);
    }
}