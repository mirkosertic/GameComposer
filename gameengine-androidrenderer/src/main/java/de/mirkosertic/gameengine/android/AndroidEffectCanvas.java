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
    public void fillRectangle(String aObjectIdentifier, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2, double aU3, double aV3, int aZIndex) {
        Path thePath = new Path();
        thePath.moveTo((float) aX0, (float) aY0);
        thePath.lineTo((float) aX1, (float) aY1);
        thePath.lineTo((float) aX2, (float) aY2);
        thePath.lineTo((float) aX3, (float) aY3);
        thePath.close();
        canvas.drawPath(thePath, currentPaint);
    }
}