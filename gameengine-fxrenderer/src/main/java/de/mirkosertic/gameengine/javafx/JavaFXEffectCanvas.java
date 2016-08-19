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
package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import javafx.scene.canvas.GraphicsContext;

public class JavaFXEffectCanvas implements EffectCanvas {

    private final GraphicsContext context;
    private Color currentColor;

    public JavaFXEffectCanvas(GraphicsContext aContext) {
        context = aContext;
    }

    private void updateCurrentPaint(Color aColor) {
        if (currentColor == null || !currentColor.equals(aColor)) {
            javafx.scene.paint.Color theFXColor = javafx.scene.paint.Color.rgb(aColor.r, aColor.g, aColor.b);
            context.setFill(theFXColor);
            context.setStroke(theFXColor);
            currentColor = aColor;
        }
    }

    @Override
    public void drawSingleDot(String aObjectID, Position aPosition, Color aColor, int aZIndex) {
        updateCurrentPaint(aColor);
        context.fillRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRectangle(String aObjectID, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2, int aX3, int aY3, Color aColor, int aZIndex) {
        updateCurrentPaint(aColor);
        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.lineTo(aX3, aY3);
        context.closePath();
        context.stroke();
        context.fill();
    }

    private void fillTriangle(String aObjectID, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2,
                              int aY2, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2) {

        JavaFXBitmapResource theImage = (JavaFXBitmapResource) aTexture;
        aU0 = aU0 * theImage.getWidth();
        aV0 = aV0 * theImage.getHeight();
        aU1 = aU1 * theImage.getWidth();
        aV1 = aV1 * theImage.getHeight();
        aU2 = aU2 * theImage.getWidth();
        aV2 = aV2 * theImage.getHeight();

        // Affine Texture Mapping
        context.save();

        context.beginPath();
        context.moveTo(aX0, aY0);
        context.lineTo(aX1, aY1);
        context.lineTo(aX2, aY2);
        context.closePath();
        context.clip();

        aX1 -= aX0; aY1 -= aY0; aX2 -= aX0; aY2 -= aY0;
        aU1 -= aU0; aV1 -= aV0; aU2 -= aU0; aV2 -= aV0;

        double id = 1.0 / (aU1*aV2 - aU2*aV1);
        double a = id * (aV2*aX1 - aV1*aX2);
        double b = id * (aV2*aY1 - aV1*aY2);
        double c = id * (aU1*aX2 - aU2*aX1);
        double d = id * (aU1*aY2 - aU2*aY1);
        double e = aX0 - a*aU0 - c*aV0;
        double f = aY0 - b*aU0 - d*aV0;

        context.transform( a, b, c, d, e, f );
        context.drawImage(theImage, 0, 0);

        context.restore();
    }

    @Override
    public void fillRectangle(String aObjectID, GameResource aTexture, int aX0, int aY0, int aX1, int aY1, int aX2,
                              int aY2, int aX3,int aY3, double aU0, double aV0, double aU1, double aV1, double aU2, double aV2, double aU3, double aV3, int aZIndex) {

        fillTriangle(aObjectID, aTexture, aX0, aY0, aX1, aY1, aX2, aY2, aU0, aV0, aU1, aV1, aU2, aV2);
        fillTriangle(aObjectID, aTexture, aX0, aY0, aX2, aY2, aX3, aY3, aU0, aV0, aU2, aV2, aU3, aV3);
    }
}