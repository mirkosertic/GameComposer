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
package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSCache;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.teavm.pixi.Container;
import de.mirkosertic.gameengine.teavm.pixi.DisplayObject;
import de.mirkosertic.gameengine.teavm.pixi.Graphics;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.teavm.pixi.Sprite;
import de.mirkosertic.gameengine.teavm.pixi.Style;
import de.mirkosertic.gameengine.teavm.pixi.Text;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;

public class TeaVMGameView extends GenericAbstractGameView<GameResource> {

    private final Renderer renderer;
    private final Map<String, DisplayObject> instances;
    private Container stage;
    private TeaVMInstanceCache instanceCache;
    private CSSCache cssCache;
    private TeaVMEffectCanvas effectCanvas;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        renderer = aRenderer;
        cssCache = new CSSCache();
        instances = new HashMap<>();
        stage = Container.createContainer();
        instanceCache = new TeaVMInstanceCache(stage);
        effectCanvas = new TeaVMEffectCanvas(instanceCache, renderer);
    }

    @Override
    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super.prepareNewScene(aGameRuntime, aCameraBehavior, aGestureDetector);

        for (Map.Entry<String, DisplayObject> theEntry : instances.entrySet()) {
            theEntry.getValue().destroy();
        }
        instances.clear();
        stage.destroy();
        stage = Container.createContainer();
        instanceCache = new TeaVMInstanceCache(stage);
        effectCanvas = new TeaVMEffectCanvas(instanceCache, renderer);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderer.setBackgroundColor(CSSUtils.toInt(aScene.backgroundColorProperty().get()));
        instanceCache.clearTouchedInstances();
        return true;
    }

    @Override
    protected void touched(GameObjectInstance aInstance) {
        instanceCache.touch(aInstance.uuidProperty().get());
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return effectCanvas;
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Angle aRotation) {
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, final Position aCenterOffset,
            final GameResource aResource) {

        String theInstanceID = aInstance.uuidProperty().get();
        Sprite theCurrentObject = instanceCache.getOrCreate(theInstanceID, () -> {
            TeaVMTextureResource theTexture = (TeaVMTextureResource) aResource;
            Sprite theSprite =  Sprite.createSprite(theTexture.getTexture());
            theSprite.getScale().set(1, 1);
            theSprite.getPivot().set(aCenterOffset.x, aCenterOffset.y);
            return theSprite;
        }, 0);

        if (aInstance.visibleProperty().get()) {
            theCurrentObject.setAlpha(1f);
        } else {
            theCurrentObject.setAlpha(0.3f);
        }

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());
    }

    @Override
    protected void drawText(String aID, Position aPosition, Angle aAngle, final Position aCenterOffset, Font aFont, Color aColor, final String aText,
            Size aSize, boolean aVisible) {

       Text theCurrentObject = instanceCache.getOrCreate(aID, () -> {
           Text theText = Text.createText(aText);
           theText.getScale().set(1, 1);
           theText.getPivot().set(aCenterOffset.x, aCenterOffset.y);
           return theText;
       }, 0);

        if (aVisible) {
            theCurrentObject.setAlpha(1f);
        } else {
            theCurrentObject.setAlpha(0.3f);
        }

        Style theStyle = theCurrentObject.getStyle();
        theStyle.setFontFamily(CSSUtils.toFontFamily(aFont));
        theStyle.setFontSize(aFont.size+"px");
        theStyle.setFill(cssCache.toColor(aColor));
        theStyle.setStroke(cssCache.toColor(aColor));
        theCurrentObject.setText(aText);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPosition.x + aCenterOffset.x, aPosition.y + aCenterOffset.y);
        theCurrentObject.setRotation(aAngle.toRadians());
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, final Position aCenterOffset, final Color aColor,
            final Size aSize) {

        String theInstanceID = aInstance.uuidProperty().get();
        Graphics theCurrentObject = instanceCache.getOrCreate(theInstanceID, () -> {
            Graphics theCurrentObject1 = Graphics.createGraphics();
            theCurrentObject1.getScale().set(1, 1);
            theCurrentObject1.getPivot().set(aCenterOffset.x, aCenterOffset.y);
            theCurrentObject1.setWidth(aSize.width);
            theCurrentObject1.setHeight(aSize.height);
            theCurrentObject1.lineStyle(1, CSSUtils.toInt(aColor), 1f);
            theCurrentObject1.drawRect(0, 0, aSize.width, aSize.height);
            return theCurrentObject1;
        }, 0);

        if (aInstance.visibleProperty().get()) {
            theCurrentObject.setAlpha(1f);
        } else {
            theCurrentObject.setAlpha(0.3f);
        }

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
    }

    @Override
    protected void framefinished() {
        super.framefinished();

        // Remove no longer visible instances
        getCurrentGameRuntime().getLogger().time("Clean invisible instances");
        instanceCache.keepOnlyTouched();
        getCurrentGameRuntime().getLogger().timeEnd("Clean invisible instances");

        // Now we sort by ZIndex
        getCurrentGameRuntime().getLogger().time("Sorting by Z-Index");
        DisplayObject[] theObjects = stage.getChildren();

        int theNumberOfObjects = stage.getChildren().length;

        for (int i=0;i<theNumberOfObjects;i++)  {
            boolean theChanged = false;
            for (int j=i+1;j<theNumberOfObjects;j++) {
                DisplayObject theObject1 = theObjects[i];
                DisplayObject theObject2 = theObjects[j];
                if (theObject1.getZOrder() < theObject2.getZOrder()) {
                    stage.swapChildren(theObject1, theObject2);
                    DisplayObject theTemp = theObjects[i];
                    theObjects[i] = theObjects[j];
                    theObjects[j] = theTemp;
                    theChanged = true;
                }
            }
            if (!theChanged) break;
        }
        getCurrentGameRuntime().getLogger().timeEnd("Sorting by Z-Index");

        getCurrentGameRuntime().getLogger().time("Pixi Rendering");
        renderer.render(stage);
        getCurrentGameRuntime().getLogger().timeEnd("Pixi Rendering");
    }

    public void setSize(Size aSize) {
        super.setCurrentScreenSize(aSize);

        renderer.resize(aSize.width, aSize.height);
    }

    protected Container getStage() {
        return stage;
    }
}