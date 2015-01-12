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

        Paint theBackgroundPaint = AndroidUtils.toPaint(aScene.backgroundColorProperty().get());
        theCanvas.drawPaint(theBackgroundPaint);
        theCanvas.drawRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight(), theBackgroundPaint);

        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new AndroidEffectCanvas(canvas);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        canvas.save();

        int theAngleInDegrees = aRotation.angleInDegrees;
        if (theAngleInDegrees % 360 != 0) {
            canvas.rotate(theAngleInDegrees, aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        }
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, AndroidBitmapResource aResource) {
        Matrix theMatrix = new Matrix();
        theMatrix.postTranslate(aPositionOnScreen.x, aPositionOnScreen.y);
        canvas.drawBitmap(aResource.bitmap, theMatrix, emptyPaint);
    }

    @Override
    protected void drawText(GameObjectInstance aInstance, Position aPosition, Position aCenterOffset, Font aFont, Color aColor, String aText, Size aSize) {
        Paint thePaint = AndroidUtils.toPaint(aColor);
        thePaint.setTextSize(aFont.size);
        thePaint.setTypeface(toTypeface(aFont));
        canvas.drawText(aText, aPosition.x, aPosition.y, thePaint);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize) {
        canvas.drawRect(aPositionOnScreen.x, aPositionOnScreen.y, aPositionOnScreen.x + aSize.width, aPositionOnScreen.y + aSize.height, AndroidUtils.toPaint(aColor));
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