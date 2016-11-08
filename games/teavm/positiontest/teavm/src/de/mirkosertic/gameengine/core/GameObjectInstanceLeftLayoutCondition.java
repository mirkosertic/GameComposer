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
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class GameObjectInstanceLeftLayoutCondition implements Condition {

    static final String TYPE_VALUE = "InstanceLeftLayoutCondition";

    public static final String GAME_OBJECT_PROPERTY = "gameObject";

    private final Property<GameObject> gameObject;

    @UsedByReflection
    public GameObjectInstanceLeftLayoutCondition() {
        gameObject = new Property<>(GameObject.class, this, GAME_OBJECT_PROPERTY, (GameObject) null);
    }

    public Property<GameObject> gameObjectProperty() {
        return gameObject;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof PropertyChanged) {
            PropertyChanged theChanged = (PropertyChanged) aEvent;
            if (GameObjectInstance.POSITION_PROPERTY.equals(theChanged.property.getName())) {
                Object theChangedObject = theChanged.getOwner();
                if (theChangedObject instanceof GameObjectInstance) {
                    GameObjectInstance theInstance = (GameObjectInstance) theChangedObject;
                    if (theInstance.getOwnerGameObject() == gameObject.get()) {
                        Rectangle theLayoutBounds = aScene.layoutBoundsProperty().get();
                        Position theNewPosition = (Position) theChanged.property.get();
                        if (!theLayoutBounds.contains(theNewPosition)) {
                            return new ConditionResult(true, aEvent, new GameObjectInstance[] {theInstance});
                        }
                    }
                }
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (!gameObject.isNull()) {
            theResult.put("gameObjectUUID", gameObject.get().uuidProperty().get());
        }
        return theResult;
    }

    public static GameObjectInstanceLeftLayoutCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        GameObjectInstanceLeftLayoutCondition theResult = new GameObjectInstanceLeftLayoutCondition();
        String theGameObjectUUID = (String) aSerializedData.get("gameObjectUUID");
        if (theGameObjectUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findObjectByID(theGameObjectUUID));
        }
        return theResult;
    }
}
