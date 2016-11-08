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

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Position;

import java.util.HashMap;
import java.util.Map;

public class SpawnGameObjectInstanceAction implements Action {

    public static final String TYPE_VALUE = "SpawnGameObjectInstanceAction";
    public static final String GAME_OBJECT_PROPERTY = "gameObject";
    public static final String REFERENCE_OBJECT_PROPERTY = "referenceObject";
    public static final String OFFSET_PROPERTY = "offset";

    private final Property<GameObject> referenceObject;
    private final Property<GameObject> gameObject;
    private final Property<Position> offset;

    @UsedByReflection
    public SpawnGameObjectInstanceAction() {
        gameObject = new Property<>(GameObject.class, this, GAME_OBJECT_PROPERTY, (GameObject) null);
        referenceObject = new Property<>(GameObject.class, this, REFERENCE_OBJECT_PROPERTY, (GameObject) null);
        offset = new Property<>(Position.class, this, OFFSET_PROPERTY, new Position(0, 0));
    }

    public Property<GameObject> gameObjectProperty() {
        return gameObject;
    }

    public Property<GameObject> referenceObjectProperty() {
        return gameObject;
    }

    public Property<Position> offsetProperty() {
        return offset;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        GameObject theObjectTemplate = gameObject.get();
        if (theObjectTemplate != null) {
            for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
                if (theInstance.getOwnerGameObject() == referenceObject.get()) {
                    Position theInstancePosition = theInstance.positionProperty().get();
                    GameObjectInstance theNewInstance = aScene.createFrom(theObjectTemplate);
                    theNewInstance.positionProperty().set(theInstancePosition.add(offset.get()));
                    aScene.addInstance(theNewInstance);
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        GameObject theObject = gameObject.get();
        if (theObject != null) {
            theResult.put("objectUuid", theObject.uuidProperty().get());
        }
        GameObject theReferenceObject = gameObject.get();
        if (theReferenceObject != null) {
            theResult.put("referenceObjectUuid", theReferenceObject.uuidProperty().get());
        }
        theResult.put(OFFSET_PROPERTY, offset.get().serialize());
        return theResult;
    }

    public static SpawnGameObjectInstanceAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SpawnGameObjectInstanceAction theResult = new SpawnGameObjectInstanceAction();
        String theObjectUUID = (String) aSerializedData.get("objectUuid");
        if (theObjectUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findObjectByID(theObjectUUID));
        }
        String theOReferemceObjectUUID = (String) aSerializedData.get("referenceObjectUuid");
        if (theOReferemceObjectUUID != null) {
            theResult.referenceObject.setQuietly(aGameScene.findObjectByID(theOReferemceObjectUUID));
        }

        Map<String, Object> thePosition = (Map<String, Object>) aSerializedData.get(OFFSET_PROPERTY);
        if (thePosition != null) {
            theResult.offset.setQuietly(Position.deserialize(thePosition));
        }
        return theResult;
    }
}