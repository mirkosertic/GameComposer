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

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class GameObjectInstanceAddedToSceneCondition implements Condition {

    static final String TYPE_VALUE = "GameObjectInstanceAddedToSceneCondition";

    public static final String GAME_OBJECT_PROPERTY = "gameObject";

    private final Property<GameObject> gameObject;

    @UsedByReflection
    public GameObjectInstanceAddedToSceneCondition() {
        gameObject = new Property<>(GameObject.class, this, GAME_OBJECT_PROPERTY, (GameObject) null);
    }

    public Property<GameObject> gameObjectProperty() {
        return gameObject;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof GameObjectInstanceAddedToScene) {
            GameObjectInstanceAddedToScene theEvent = (GameObjectInstanceAddedToScene) aEvent;
            GameObjectInstance theInstance = theEvent.instance;
            if (theInstance.getOwnerGameObject() == gameObject.get()) {
                return new ConditionResult(true, aEvent, new GameObjectInstance[] {theInstance});
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        GameObject theObject = gameObject.get();
        if (theObject != null) {
            theResult.put("objectUUID", theObject.uuidProperty().get());
        }
        return theResult;
    }

    public static GameObjectInstanceAddedToSceneCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        GameObjectInstanceAddedToSceneCondition theResult = new GameObjectInstanceAddedToSceneCondition();
        String theObjectUUID = (String) aSerializedData.get("objectUUID");
        if (theObjectUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findObjectByID(theObjectUUID));
        }
        return theResult;
    }
}
