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
package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.AbstractGameProcess;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

public class FollowCameraProcess extends AbstractGameProcess {

    private final GameObjectInstance cameraObject;
    private final GameObjectInstance playerObject;

    FollowCameraProcess(GameObjectInstance aCameraObject, GameObjectInstance aPlayerInstance) {
        cameraObject = aCameraObject;
        playerObject = aPlayerInstance;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        CameraBehavior theCameraBehavior = cameraObject.getBehavior(CameraBehavior.TYPE);
        Position theCameraPosition = cameraObject.positionProperty().get();
        Size theCameraSize = theCameraBehavior.getScreenSize();

        Position theCameraCenter = new Position(theCameraPosition.x + theCameraSize.width / 2, theCameraPosition.y + theCameraSize.height / 2);

        Position thePlayerPosition = playerObject.positionProperty().get();
        Size thePlayerSize = playerObject.getOwnerGameObject().sizeProperty().get();

        Position thePlayerCenter = new Position(thePlayerPosition.x + thePlayerSize.width / 2, thePlayerPosition.y + thePlayerSize.height / 2);

        float theDX = thePlayerCenter.x - theCameraCenter.x;
        float theDY = thePlayerCenter.y - theCameraCenter.y;

        float theMX = theDX / 300 * aElapsedTimeSinceLastLoop;
        float theMY = theDY / 300 * aElapsedTimeSinceLastLoop;

        cameraObject.positionProperty().set(new Position(theCameraPosition.x + theMX, theCameraPosition.y + theMY));

        return ProceedResult.CONTINUE_RUNNING;
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        String theInstanceUID = aInstance.uuidProperty().get();
        return cameraObject.uuidProperty().get().equals(theInstanceUID)
                || playerObject.uuidProperty().get().equals(theInstanceUID);
    }
}
