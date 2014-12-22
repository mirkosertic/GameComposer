package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.*;

public class AndroidGameView extends GenericAbstractGameView<AndroidBitmapResource> {

    private final AndroidCanvas androidCanvas;
    private final Typeface arialTypeface;
    private final Typeface verdanaTypeface;

    private final Paint emptyPaint;

    AndroidGameView(AndroidCanvas aCanvas, CameraBehavior aCameraBehavior, GameRuntime aRuntime, GestureDetector aGestureDetector) {
        super(aRuntime, aCameraBehavior, aGestureDetector);
        androidCanvas = aCanvas;

        emptyPaint = new Paint();

        arialTypeface = Typeface.create("Arial", Typeface.NORMAL);
        verdanaTypeface = Typeface.create("Verdana", Typeface.NORMAL);
    }

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    @Override
    protected boolean beginFrame(GameScene aScene) {
        surfaceHolder = androidCanvas.getHolder();
        if (surfaceHolder.isCreating()) {
            return false;
        }
        Canvas theCanvas = surfaceHolder.lockCanvas();
        if (theCanvas == null) {
            // Cannot lock
            return false;
        }

        canvas = theCanvas;

        Paint theBackgroundPaint = toPaint(aScene.backgroundColorProperty().get());
        theCanvas.drawPaint(theBackgroundPaint);
        theCanvas.drawRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight(), theBackgroundPaint);

        return true;
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, float aOffsetX, float aOffsetY, Angle aRotation) {
        canvas.save();

        int theAngleInDegrees = aRotation.angleInDegrees;
        if (theAngleInDegrees % 360 != 0) {
            canvas.rotate(theAngleInDegrees, aOffsetX, aOffsetY);
        }
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, AndroidBitmapResource aResource, float aPositionX, float aPositionY) {
        Matrix theMatrix = new Matrix();
        theMatrix.postTranslate(aPositionX, aPositionY);
        canvas.drawBitmap(aResource.bitmap, theMatrix, emptyPaint);
    }

    @Override
    protected void drawText(GameObjectInstance aInstance, Position aPosition, Font aFont, Color aColor, String aText, Size aSize) {
        Paint thePaint = toPaint(aColor);
        thePaint.setTextSize(aFont.size);
        thePaint.setTypeface(toTypeface(aFont));
        canvas.drawText(aText, aPosition.x, aPosition.y, thePaint);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Color aColor, float aX, float aY, float aWidth, float aHeight) {
        canvas.drawRect(aX, aY, aX + aWidth, aY + aHeight, toPaint(aColor));
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        canvas.restore();
    }

    @Override
    protected void framefinished() {
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    protected void logError(String aMessage) {
        // TODO: Android logging implement herre
        throw new RuntimeException(aMessage);
    }

    private Paint toPaint(de.mirkosertic.gameengine.type.Color aColor) {
        Paint thePaint = new Paint();
        thePaint.setARGB(255, aColor.r, aColor.g, aColor.b);
        thePaint.setStyle(Paint.Style.FILL);
        return thePaint;
    }

    private Typeface toTypeface(Font aFont) {
        switch (aFont.name) {
            case VERDANA:
                return verdanaTypeface;
            case ARIAL:
                return arialTypeface;
        }
        throw new IllegalArgumentException("Unknown font : " + aFont.name);
    }
}