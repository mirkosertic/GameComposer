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
package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.CollisionPosition;

import java.util.HashMap;
import java.util.Map;

public class ObjectCollisionCondition implements Condition {

    static final String TYPE_VALUE = "ObjectCollisionCondition";

    public static final String PRIMARY_OBJECT_PROPERTY = "primaryObject";
    public static final String SECONDARY_OBJECT_PROPERTY = "secondaryObject";
    public static final String POSITION_PROPERTY = "position";

    private final Property<GameObject> primaryObject;
    private final Property<GameObject> secondaryObject;
    private final Property<CollisionPosition> position;

    @UsedByReflection
    public ObjectCollisionCondition() {
        primaryObject = new Property<>(GameObject.class, this, PRIMARY_OBJECT_PROPERTY, (GameObject) null);
        secondaryObject = new Property<>(GameObject.class, this, SECONDARY_OBJECT_PROPERTY, (GameObject) null);
        position = new Property<>(CollisionPosition.class, this, POSITION_PROPERTY, CollisionPosition.EVERYWHERE);
    }

    public Property<GameObject> primaryObjectProperty() {
        return primaryObject;
    }

    public Property<GameObject> secondaryObjectProperty() {
        return secondaryObject;
    }

    public Property<CollisionPosition> positionProperty() {
        return position;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof GameObjectCollision) {
            if (!primaryObject.isNull() && !secondaryObject.isNull()) {
                GameObject thePrimaryObject = primaryObject.get();
                GameObject theSecondaryObject = secondaryObject.get();

                GameObjectCollision theCollision = (GameObjectCollision) aEvent;

                GameObject theO1 = theCollision.instance1.getOwnerGameObject();
                GameObject theO2 = theCollision.instance2.getOwnerGameObject();

                CollisionPosition thePosition = position.get();

                // Case 1
                if (thePrimaryObject.uuidProperty().get().equals(theO1.uuidProperty().get()) &&
                        theSecondaryObject.uuidProperty().get().equals(theO2.uuidProperty().get())) {
                    // Collision
                    if (thePosition.detect(theCollision.instance1.positionProperty().get(), theCollision.instance2.positionProperty().get())) {
                        return new ConditionResult(true, aEvent, new GameObjectInstance[] {theCollision.instance1});
                    }
                }

                if (theSecondaryObject.uuidProperty().get().equals(theO1.uuidProperty().get()) &&
                        thePrimaryObject.uuidProperty().get().equals(theO2.uuidProperty().get())) {
                    // Collision
                    if (thePosition.detect(theCollision.instance2.positionProperty().get(), theCollision.instance1.positionProperty().get())) {
                        return new ConditionResult(true, aEvent, new GameObjectInstance[] {((GameObjectCollision) aEvent).instance2});
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
        if (!primaryObject.isNull()) {
            theResult.put("primaryObjectUuid", primaryObject.get().uuidProperty().get());
        }
        if (!secondaryObject.isNull()) {
            theResult.put("secondaryObjectUuid", secondaryObject.get().uuidProperty().get());
        }
        if (!position.isNull()) {
            theResult.put(POSITION_PROPERTY, position.get().name());
        }
        return theResult;
    }

    public static ObjectCollisionCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        ObjectCollisionCondition theCondition = new ObjectCollisionCondition();
        String thePrimaryObject = (String) aSerializedData.get("primaryObjectUuid");
        if (thePrimaryObject != null) {
            theCondition.primaryObject.setQuietly(aGameScene.findObjectByID(thePrimaryObject));
        }
        String theSecondaryObject = (String) aSerializedData.get("secondaryObjectUuid");
        if (theSecondaryObject != null) {
            theCondition.secondaryObject.setQuietly(aGameScene.findObjectByID(theSecondaryObject));
        }
        String thePositon = (String) aSerializedData.get(POSITION_PROPERTY);
        if (thePositon != null) {
            theCondition.position.setQuietly(CollisionPosition.valueOf(thePositon));
        }
        return theCondition;
    }
}