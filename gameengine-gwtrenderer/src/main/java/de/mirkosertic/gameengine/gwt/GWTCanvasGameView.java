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
package de.mirkosertic.gameengine.gwt;

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

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class GWTCanvasGameView extends GenericAbstractGameView<GWTBitmapResource> {

    private final Canvas canvas;

    public GWTCanvasGameView(GameRuntime aGameRuntime, Canvas aCanvas, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        canvas = aCanvas;
    }

    private Context2d context2d;
    private GWTCanvasUtils.SavedState savedState;

    @Override
    protected boolean beginFrame(GameScene aScene) {
        context2d = canvas.getContext2d();
        savedState = GWTCanvasUtils.saveState(context2d);

        Color theBGColor = aScene.backgroundColorProperty().get();
        Size theCurrentSize = getCurrentScreenSize();

        CssColor theCssBackground = CssColor.make(theBGColor.r, theBGColor.g, theBGColor.b);
        context2d.setFillStyle(theCssBackground);
        context2d.setStrokeStyle(theCssBackground);
        context2d.fillRect(0, 0, theCurrentSize.width, theCurrentSize.height);

        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new GWTEffectCanvas(context2d);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        context2d.translate(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        context2d.rotate(aRotation.toRadians());
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, GWTBitmapResource aResource) {
        if (aResource.isLoaded()) {
            context2d.drawImage(aResource.getImage(), -aCenterOffset.x, -aCenterOffset.y);
        } else {
            Size theSize = aInstance.getOwnerGameObject().sizeProperty().get();
            context2d.setFillStyle(CssColor.make(255, 255, 255));
            context2d.setStrokeStyle(CssColor.make(255, 255, 255));
            context2d.setLineWidth(1);
            context2d.strokeRect(-aCenterOffset.x, -aCenterOffset.y, theSize.width, theSize.height);
        }

    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont, Color aColor, String aText,
            Size aSize, boolean aVisible) {
        GWTCanvasUtils.drawText(context2d, aPosition, aFont, aColor, aText, aSize);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScren, Position aCenterOffset, Color aColor, Size aSize) {
        context2d.setFillStyle(CssColor.make(255, 255, 255));
        context2d.setStrokeStyle(CssColor.make(255, 255, 255));
        context2d.setLineWidth(1);
        context2d.strokeRect(-aCenterOffset.x, -aCenterOffset.y, aSize.width, aSize.height);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        GWTCanvasUtils.restoreState(context2d, savedState);
    }
}