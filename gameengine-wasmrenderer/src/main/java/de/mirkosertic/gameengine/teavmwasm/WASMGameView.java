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
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

public class WASMGameView extends GenericAbstractGameView {

    public WASMGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        return false;
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
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new WASMEffectCavas();
    }
}