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

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventType;

import java.util.HashMap;
import java.util.Map;

public class NewGameInstance extends GameEvent implements DistributableEvent {

    public static final GameEventType TYPE = new GameEventType("NewGameInstance");

    private final GameObjectInstance player;

    public NewGameInstance(GameObjectInstance aPlayer) {
        super(TYPE);
        player = aPlayer;
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, TYPE.getType());
        if (player != null) {
            GameObjectInstanceAddedToScene theEvent = new GameObjectInstanceAddedToScene(player);
            theResult.put("playerAddEvent", theEvent.serialize());
        }
        return theResult;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {

        Map<String, Object> thePlayerAddEvent = (Map<String, Object>) aEventData.get("playerAddEvent");
        if (thePlayerAddEvent != null) {
            GameObjectInstanceAddedToScene.runEventInScene(thePlayerAddEvent, aGameScene);
        }

        NewGameInstance theInstance = new NewGameInstance(null);
        aGameScene.getRuntime().getEventManager().fire(theInstance);
    }
}
