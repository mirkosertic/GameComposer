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

import de.mirkosertic.gameengine.bytecoder.pixi.Container;
import de.mirkosertic.gameengine.bytecoder.pixi.DisplayObject;
import de.mirkosertic.gameengine.bytecoder.pixi.Graphics;
import de.mirkosertic.gameengine.bytecoder.pixi.Renderer;
import de.mirkosertic.gameengine.bytecoder.pixi.Sprite;
import de.mirkosertic.gameengine.bytecoder.pixi.Style;
import de.mirkosertic.gameengine.bytecoder.pixi.Text;
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

import java.util.HashMap;
import java.util.Map;

public class BytecoderGameView extends GenericAbstractGameView {

    private final Renderer renderer;
    private final Map<String, DisplayObject> instances;
    private Container stage;
    private BytecoderInstanceCache instanceCache;
    private final CSSCache cssCache;

    public BytecoderGameView(final GameRuntime aGameRuntime,
                             final CameraBehavior aCameraBehavior,
                             final GestureDetector aGestureDetector,
                             final Renderer renderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        this.renderer = renderer;
        cssCache = new CSSCache();
        instances = new HashMap<>();
        stage = Container.createContainer();
        instanceCache = new BytecoderInstanceCache(stage);
    }

    @Override
    public void prepareNewScene(final GameRuntime aGameRuntime, final CameraBehavior aCameraBehavior, final GestureDetector aGestureDetector) {
        super.prepareNewScene(aGameRuntime, aCameraBehavior, aGestureDetector);

        for (final Map.Entry<String, DisplayObject> theEntry : instances.entrySet()) {
            theEntry.getValue().destroyDisplayObject();
        }
        instances.clear();
        stage.destroyDisplayObject();
        stage = Container.createContainer();
        instanceCache = new BytecoderInstanceCache(stage);
    }

    @Override
    protected boolean beginFrame(final GameScene aScene) {
        renderer.backgroundColor(CSSUtils.toInt(aScene.backgroundColorProperty().get()));
        instanceCache.clearTouchedInstances();
        return true;
    }

    @Override
    protected void touched(final GameObjectInstance aInstance) {
        instanceCache.touch(aInstance.uuidProperty().get());
    }

    @Override
    protected void beforeInstance(final GameObjectInstance aInstance, final Position aPositionOnScreen, final Position aCenterOffset,
                                  final Angle aRotation) {
    }

    @Override
    protected void drawImage(final GameObjectInstance aInstance, final Position aPositionOnScreen, final Position aCenterOffset,
                             final GameResource aResource) {
        final String theInstanceID = aInstance.uuidProperty().get();
        final Sprite theCurrentObject = instanceCache.getOrCreate(theInstanceID, () -> {
            final BytecoderTextureResource theTexture = (BytecoderTextureResource) aResource;
            final Sprite theSprite =  Sprite.createSprite(theTexture.getTexture());
            theSprite.scale().set(1, 1);
            theSprite.pivot().set(aCenterOffset.x, aCenterOffset.y);
            return theSprite;
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
    protected void drawText(final String aID, final Position aPositionOnScreen, final Angle aAngle, final Position aCenterOffset, final Font aFont,
                            final Color aColor, final String aText, final Size aSize, final boolean aVisible) {
        final Text theCurrentObject = instanceCache.getOrCreate(aID, () -> {
            final Text theText = Text.createText(aText);
            theText.scale().set(1, 1);
            theText.pivot().set(aCenterOffset.x, aCenterOffset.y);
            return theText;
        }, 0);

        if (aVisible) {
            theCurrentObject.alpha(1f);
        } else {
            theCurrentObject.alpha(0.3f);
        }

        final Style theStyle = theCurrentObject.style();
        theStyle.fontFamily(CSSUtils.toFontFamily(aFont));
        theStyle.fontSize(aFont.size+"px");
        theStyle.fill(cssCache.toColor(aColor));
        theStyle.stroke(cssCache.toColor(aColor));
        theCurrentObject.text(aText);

        // Update the position and all the other stuff
        theCurrentObject.position().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.rotation(aAngle.toRadians());
    }

    @Override
    protected void drawRect(final GameObjectInstance aInstance, final Position aPositionOnScreen, final Position aCenterOffset,
                            final Color aColor, final Size aSize) {
        final String theInstanceID = aInstance.uuidProperty().get();
        final Graphics theCurrentObject = instanceCache.getOrCreate(theInstanceID, () -> {
            final Graphics theCurrentObject1 = Graphics.createGraphics();
            theCurrentObject1.scale().set(1, 1);
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
    protected void afterInstance(final GameObjectInstance aInstance, final Position aPositionOnScreen) {
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
            public void drawSingleDot(final String aObjectIdentifier, final Position aPosition, final Color aColor, final int aZIndex) {
            }

            @Override
            public void fillRectangle(final String aObjectIdentifier, final int aX0, final int aY0, final int aX1, final int aY1, final int aX2, final int aY2,
                                      final int aX3, final int aY3, final Color aColor, final int aZIndex) {
            }

            @Override
            public void fillRectangle(final String aObjectIdentifier, final GameResource aTexture, final int aX0, final int aY0, final int aX1,
                                      final int aY1, final int aX2, final int aY2, final int aX3, final int aY3, final double aU0, final double aV0, final double aU1, final double aV1, final double aU2,
                                      final double aV2, final double aU3, final double aV3, final int aZIndex) {
            }
        };
    }

    public void setSize(final Size screenSize) {
        super.setCurrentScreenSize(screenSize);
        renderer.resize(screenSize.width, screenSize.height);
    }
}