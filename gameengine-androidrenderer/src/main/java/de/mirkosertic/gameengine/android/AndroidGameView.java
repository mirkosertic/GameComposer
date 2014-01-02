package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;

import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteComponent;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextComponent;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;

public class AndroidGameView implements GameView {

    private final AndroidCanvas canvas;
    private final CameraComponent cameraComponent;
    private final GameRuntime gameRuntime;
    private final Typeface arialTypeface;
    private final Typeface verdanaTypeface;
    private final GestureDetector gestureDetector;

    private final Paint emptyPaint;
    private final Paint whitePaint;

    AndroidGameView(AndroidCanvas aCanvas, CameraComponent aCameraComponent, GameRuntime aRuntime) {
        canvas = aCanvas;
        cameraComponent = aCameraComponent;
        gameRuntime = aRuntime;
        gestureDetector = new DefaultGestureDetector(aRuntime.getEventManager());
        emptyPaint = new Paint();

        whitePaint = new Paint();
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setColor(android.graphics.Color.WHITE);
        whitePaint.setStrokeWidth(1);

        arialTypeface = Typeface.create("Arial", Typeface.NORMAL);
        verdanaTypeface = Typeface.create("Verdana", Typeface.NORMAL);
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {

        SurfaceHolder theHolder = canvas.getHolder();
        if (theHolder.isCreating()) {
            // Nothing to do here
            return;
        }
        Canvas theCanvas = theHolder.lockCanvas();
        if (theCanvas == null) {
            // Cannot lock
            return;
        }

        Paint theBackgroundPaint = toPaint(aScene.backgroundColorProperty().get());
        theCanvas.drawPaint(theBackgroundPaint);
        theCanvas.drawRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight(), theBackgroundPaint);

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            theCanvas.save();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;
            if (theAngleInDegrees % 360 != 0) {
                theCanvas.rotate(theAngleInDegrees, thePosition.x + theHalfWidth, thePosition.y + theHalfHeight);
            }

            boolean theSomethingRendered = false;

            Sprite theTemplateComponent = theInstance.getComponent(SpriteComponent.class);
            if (theTemplateComponent != null && !theTemplateComponent.resourceNameProperty().isNull()) {
                try {
                    AndroidBitmapResource theBitmap = gameRuntime.getResourceCache().getResourceFor(
                            theTemplateComponent.resourceNameProperty().get());
                    drawGameObjectInstance(theCanvas, theInstance, thePosition, theSize, theBitmap);

                    theSomethingRendered = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Text theTextComponent = theInstance.getComponent(TextComponent.class);
            if (theTextComponent != null) {
                drawText(theCanvas, theInstance, thePosition, theTextComponent.fontProperty().get(), theTextComponent.colorProperty().get(), theTextComponent.textExpressionProperty().get(), theSize);
                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                theCanvas.drawRect(thePosition.x, thePosition.y, thePosition.x + theSize.width, thePosition.y + theSize.height, whitePaint);
            }

            theCanvas.restore();
        }

        theHolder.unlockCanvasAndPost(theCanvas);
    }

    private void drawText(Canvas aCanvas, GameObjectInstance aInstance, Position aPosition, Font aFont, Color aColor, TextExpression aTextExpression, Size aSize) {
        aCanvas.save();
        Paint thePaint = toPaint(aColor);
        thePaint.setTextSize(aFont.size);
        thePaint.setTypeface(toTypeface(aFont));
        aCanvas.drawText(aTextExpression.expression, aPosition.x, aPosition.y, thePaint);
        aCanvas.restore();
    }

    private void drawGameObjectInstance(Canvas aCanvas, GameObjectInstance aInstance, Position aPosition, Size aSize, AndroidBitmapResource aBitmap) {
        Matrix theMatrix = new Matrix();
        theMatrix.postTranslate(aPosition.x, aPosition.y);
        aCanvas.drawBitmap(aBitmap.bitmap, theMatrix, emptyPaint);
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
        throw new IllegalArgumentException("Unknown font : "+aFont.name);
    }
}