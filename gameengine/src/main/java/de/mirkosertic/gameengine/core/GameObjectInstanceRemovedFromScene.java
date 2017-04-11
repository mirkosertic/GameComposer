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
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventType;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class GameObjectInstanceRemovedFromScene extends GameEvent implements DistributableEvent {

    public static final GameEventType TYPE = new GameEventType("GameObjectInstanceRemovedFromScene");

    private static final GameObjectInstanceRemovedFromSceneClassInformation CIINSTANCE = new GameObjectInstanceRemovedFromSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceRemovedFromScene(GameScene aScene, GameObjectInstance aInstance) {
        super(TYPE);
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameObjectInstanceRemovedFromSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type.getType());
        theResult.put("instanceID", instance.uuidProperty().get());
        return theResult;
    }

    @Override
    public boolean isForced() {
        return true;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        String theInstanceID = (String) aEventData.get("instanceID");
        GameObjectInstance theInstance = aGameScene.findInstanceByID(theInstanceID);
        if (theInstance != null) {
            aGameScene.removeGameObjectInstance(theInstance);
        }
    }
}