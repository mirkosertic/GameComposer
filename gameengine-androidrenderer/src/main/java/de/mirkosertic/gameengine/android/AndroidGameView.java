/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

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

    private Canvas canvas;

    @Override
    protected boolean beginFrame(GameScene aScene) {
        Canvas theCanvas = androidCanvas.getCurrentCanvas();
        if (theCanvas == null) {
            // Cannot lock
            return false;
        }

        canvas = theCanvas;

        Paint theBackgroundPaint = AndroidUtils.toFilledPaint(aScene.backgroundColorProperty().get());
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

        canvas.rotate(aRotation.angleInDegrees, aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, AndroidBitmapResource aResource) {
        Matrix theMatrix = new Matrix();
        theMatrix.postTranslate(aPositionOnScreen.x, aPositionOnScreen.y);
        canvas.drawBitmap(aResource.bitmap, theMatrix, emptyPaint);
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont, Color aColor, String aText,
            Size aSize, boolean aVisible) {
        Paint thePaint = AndroidUtils.toFilledPaint(aColor);
        thePaint.setTextSize(aFont.size);
        thePaint.setTypeface(toTypeface(aFont));
        canvas.drawText(aText, aPosition.x, aPosition.y + aFont.size, thePaint);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize) {
        canvas.drawRect(aPositionOnScreen.x, aPositionOnScreen.y, aPositionOnScreen.x + aSize.width, aPositionOnScreen.y + aSize.height, AndroidUtils.toStrokePaint(aColor));
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        canvas.restore();
    }

    @Override
    protected void framefinished() {
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