/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSCache;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import org.teavm.interop.Import;

public class WASMGameView extends GenericAbstractGameView {

    @Import(module = "engine", name = "beginFrame")
    public static native void externalBeginFrame();

    @Import(module = "engine", name = "drawText")
    public static native void externalDrawText(String aObjectID, String aText, String aFontFamily, String aFontSize,
                                               String aCSSFill,
                                               String aCSSStroke,
                                               float aX,
                                               float aY,
                                               float aCenterX,
                                               float aCentery,
                                               float aRotation);

    @Import(module = "engine", name = "drawImage")
    public static native void externalDrawImage(String aObjectID, int aTextureID,
            float aX,
            float aY,
            float aCenterX,
            float aCentery,
            float aRotation);

    @Import(module = "engine", name = "drawRect")
    public static native void externalDrawRect(String aObjectID, int aWidth, int aHeight, int aColor, float aX, float aY,
            float aCenterX, float aCenterY, float aRotation);

    @Import(module = "engine", name = "frameFinished")
    public static native void externalFrameFinished();

    private final CSSCache cssCache;

    public WASMGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        cssCache = new CSSCache();
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        externalBeginFrame();
        return true;
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Angle aRotation) {
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            GameResource aResource) {

        String theInstanceID = aInstance.uuidProperty().get();
        float theX = aPositionOnScreen.x + aCenterOffset.x;
        float theY = aPositionOnScreen.y + aCenterOffset.y;
        float theRotationRad = aInstance.rotationAngleProperty().get().toRadians();
        WASMTextureResource thetextureResource = (WASMTextureResource) aResource;

        externalDrawImage(theInstanceID, thetextureResource.getExternalID(),
                theX, theY, aCenterOffset.x, aCenterOffset.y, theRotationRad);
    }

    @Override
    protected void drawText(String aID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont,
            Color aColor, String aText, Size aSize, boolean aVisible) {

        String theFontFamily = CSSUtils.toFontFamily(aFont);
        String theFontSize = aFont.size+"px";
        String theFill = cssCache.toColor(aColor);
        String theStroke = cssCache.toColor(aColor);
        float theX = aPosition.x + aCenterOffset.x;
        float theY = aPosition.y + aCenterOffset.y;
        float theRotation = aAngle.toRadians();

        externalDrawText(aID, aText, theFontFamily, theFontSize, theFill, theStroke, theX
        , theY, aCenterOffset.x, aCenterOffset.y, theRotation);

    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Color aColor, Size aSize) {

        String theInstanceID = aInstance.uuidProperty().get();
        int theColor = CSSUtils.toInt(aColor);
        float theX = aPositionOnScreen.x + aCenterOffset.x;
        float theY = aPositionOnScreen.y + aCenterOffset.y;
        float theRotationRad = aInstance.rotationAngleProperty().get().toRadians();
        int theWidth = aSize.width;
        int theHeight = aSize.height;

        externalDrawRect(theInstanceID, theWidth, theHeight, theColor, theX, theY,
                aCenterOffset.x, aCenterOffset.y, theRotationRad);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
    }

    @Override
    protected void framefinished() {
        super.framefinished();
        externalFrameFinished();
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new WASMEffectCavas();
    }
}