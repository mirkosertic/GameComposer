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
package de.mirkosertic.gameengine.dragome;

import com.dragome.web.enhancers.jsdelegate.JsCast;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSCache;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import org.w3c.dom.html.CanvasRenderingContext2D;
import org.w3c.dom.html.HTMLCanvasElement;
import org.w3c.dom.html.HTMLImageElement;

class DragomeGameView extends GenericAbstractGameView<DragomeGameResource> {

    private final HTMLCanvasElement canvas;
    private final CSSCache cssCache;

    public DragomeGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector, HTMLCanvasElement aCanvas) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        canvas = aCanvas;
        cssCache = new CSSCache();
    }

    private CanvasRenderingContext2D renderingContext2D;

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new DragomeEffectCanvas(renderingContext2D, cssCache);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderingContext2D = JsCast.castTo(canvas.getContext("2d"), CanvasRenderingContext2D.class);
        Size theCurrentScreenSize = getCurrentScreenSize();
        renderingContext2D.setFillStyle(cssCache.toColor(aScene.backgroundColorProperty().get()));
        renderingContext2D.fillRect(0, 0, theCurrentScreenSize.width, theCurrentScreenSize.height);
        return true;
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        renderingContext2D.save();
        renderingContext2D.translate(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        renderingContext2D.rotate(aRotation.toRadians());
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, DragomeGameResource aResource) {
		HTMLImageElement imageSource= (HTMLImageElement) aResource.getElement();
        renderingContext2D.drawImage(imageSource, -aCenterOffset.x, -aCenterOffset.y);
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont, Color aColor, String aText,
            Size aSize, boolean aVisible) {
        String theTextColor = cssCache.toColor(aColor);
        renderingContext2D.setFillStyle(theTextColor);
        renderingContext2D.setStrokeStyle(theTextColor);
        renderingContext2D.setFont(cssCache.toFont(aFont));
        renderingContext2D.fillText(aText, -aCenterOffset.x, aFont.size - aCenterOffset.y);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize) {
        String theColor = cssCache.toColor(aColor);
        renderingContext2D.setFillStyle(theColor);
        renderingContext2D.setStrokeStyle(theColor);
        renderingContext2D.setLineWidth(1);
        renderingContext2D.strokeRect(-aCenterOffset.x, -aCenterOffset.y, aSize.width, aSize.height);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        renderingContext2D.restore();
    }

    @Override
    public void setCurrentScreenSize(Size aCurrentScreenSize) {
        super.setCurrentScreenSize(aCurrentScreenSize);

        canvas.setWidth(aCurrentScreenSize.width);
        canvas.setHeight(aCurrentScreenSize.height);
    }
}