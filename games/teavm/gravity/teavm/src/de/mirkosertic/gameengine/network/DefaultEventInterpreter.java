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
package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.PropertyChanged;

import java.util.Map;

public class DefaultEventInterpreter implements EventInterpreter {

    @Override
    public void process(Map<String, Object> aEventData, GameScene aGameScene) {
        Object theEventID = aEventData.get(DistributableEvent.EVENT_ID_ATTRIBUTE);
        if (PropertyChanged.TYPE.getType().equals(theEventID)) {
            PropertyChanged.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (GameObjectInstanceAddedToScene.TYPE.getType().equals(theEventID)) {
            GameObjectInstanceAddedToScene.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (GameObjectInstanceRemovedFromScene.TYPE.getType().equals(theEventID)) {
            GameObjectInstanceRemovedFromScene.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (NewGameInstance.TYPE.getType().equals(theEventID)) {
            NewGameInstance.runEventInScene(aEventData, aGameScene);
            return;
        }
    }
}