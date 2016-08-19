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

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.TouchPosition;

/**
 * Special GestureDetector to emulate mouse events on touch devices.
 */
public class AndroidGestureDetector extends DefaultGestureDetector {

    public AndroidGestureDetector(GameEventManager aEventManager, CameraBehavior aCameraBehavior) {
        super(aEventManager, aCameraBehavior);
    }

    @Override
    public void touchStarted(TouchPosition[] aTouchPositions) {
        super.touchStarted(aTouchPositions);
        // For every finger, fire a mouse event also
        for (TouchPosition thePosition : aTouchPositions) {
            mousePressed(new Position(thePosition.x, thePosition.y));
        }
    }

    @Override
    public void touchEnded(TouchPosition[] aTouchPositions) {
        super.touchEnded(aTouchPositions);
        // For every finger, fire a mouse event also
        for (TouchPosition thePosition : aTouchPositions) {
            mouseReleased(new Position(thePosition.x, thePosition.y));
        }
    }
}
