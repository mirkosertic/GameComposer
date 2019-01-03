/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.bytecoder.pixi.Container;
import de.mirkosertic.gameengine.bytecoder.pixi.DisplayObject;
import de.mirkosertic.gameengine.bytecoder.pixi.Graphics;
import de.mirkosertic.gameengine.bytecoder.pixi.Renderer;
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

public class BytecoderGameView extends GenericAbstractGameView {

    private final Renderer renderer;
    private final Map<String, DisplayObject> instances;
    private Container stage;
    private BytecoderInstanceCache instanceCache;
    private CSSCache cssCache;

    public BytecoderGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector,
            Renderer renderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        this.renderer = renderer;
        cssCache = new CSSCache();
        instances = new HashMap<>();
        stage = Container.createContainer();
        instanceCache = new BytecoderInstanceCache(stage);
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
        instanceCache = new BytecoderInstanceCache(stage);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderer.backgroundColor(CSSUtils.toInt(aScene.backgroundColorProperty().get()));
        instanceCache.clearTouchedInstances();
        return true;
    }

    @Override
    protected void touched(GameObjectInstance aInstance) {
        instanceCache.touch(aInstance.uuidProperty().get());
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Angle aRotation) {
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            GameResource aResource) {
    }

    @Override
    protected void drawText(String aID, Position aPositionOnScreen, Angle aAngle, Position aCenterOffset, Font aFont,
            Color aColor, String aText, Size aSize, boolean aVisible) {
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Color aColor, Size aSize) {
        String theInstanceID = aInstance.uuidProperty().get();
        Graphics theCurrentObject = instanceCache.getOrCreate(theInstanceID, () -> {
            Graphics theCurrentObject1 = Graphics.createGraphics();
            theCurrentObject1.getScale().set(1, 1);
            theCurrentObject1.pivot().set(aCenterOffset.x, aCenterOffset.y);
            theCurrentObject1.width(aSize.width);
            theCurrentObject1.height(aSize.height);
            theCurrentObject1.lineStyle(1, CSSUtils.toInt(aColor), 1f);
            theCurrentObject1.drawRect(0, 0, aSize.width, aSize.height);
            return theCurrentObject1;
        }, 0);

        if (aInstance.visibleProperty().get()) {
            theCurrentObject.alpha(1f);
        } else {
            theCurrentObject.alpha(0.3f);
        }

        // Update the position and all the other stuff
        theCurrentObject.position().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.rotation(aInstance.rotationAngleProperty().get().toRadians());
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

        getCurrentGameRuntime().getLogger().time("Pixi Rendering");
        renderer.render(stage);
        getCurrentGameRuntime().getLogger().timeEnd("Pixi Rendering");
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new EffectCanvas() {
            @Override
            public void drawSingleDot(String aObjectIdentifier, Position aPosition, Color aColor, int aZIndex) {
            }

            @Override
            public void fillRectangle(String aObjectIdentifier, int aX0, int aY0, int aX1, int aY1, int aX2, int aY2,
                    int aX3, int aY3, Color aColor, int aZIndex) {
            }

            @Override
            public void fillRectangle(String aObjectIdentifier, GameResource aTexture, int aX0, int aY0, int aX1,
                    int aY1, int aX2, int aY2, int aX3, int aY3, double aU0, double aV0, double aU1, double aV1, double aU2,
                    double aV2, double aU3, double aV3, int aZIndex) {
            }
        };
    }

    public void setSize(Size screenSize) {
        super.setCurrentScreenSize(screenSize);
        renderer.resize(screenSize.width, screenSize.height);
    }
}
