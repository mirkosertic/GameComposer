package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectCollisionCondition implements Condition {

    static final String TYPE_VALUE = "ObjectCollisionCondition";

    private final Property<GameObject> primaryObject;
    private final Property<GameObject> secondaryObject;

    public ObjectCollisionCondition() {
        primaryObject = new Property<GameObject>(this, "primaryObject", (GameObject) null);
        secondaryObject = new Property<GameObject>(this, "secondaryObject", (GameObject) null);
    }

    public Property<GameObject> primaryObjectProperty() {
        return primaryObject;
    }

    public Property<GameObject> secondaryObjectProperty() {
        return secondaryObject;
    }

    @Override
    public ConditionResult appliesTo(GameEvent aEvent) {
        if (aEvent instanceof GameObjectCollision) {
            if (!primaryObject.isNull() && !secondaryObject.isNull()) {
                GameObject thePrimaryObject = primaryObject.get();
                GameObject theSecondaryObject = secondaryObject.get();

                GameObject theO1 = ((GameObjectCollision) aEvent).instance1Property().get().getOwnerGameObject();
                GameObject theO2 = ((GameObjectCollision) aEvent).instance2Property().get().getOwnerGameObject();

                // Case 1
                if (thePrimaryObject.uuidProperty().get().equals(theO1.uuidProperty().get()) &&
                        theSecondaryObject.uuidProperty().get().equals(theO2.uuidProperty().get())) {
                    // Collision
                    List<GameObjectInstance> theAffectedInstances = new ArrayList<GameObjectInstance>();
                    theAffectedInstances.add(((GameObjectCollision) aEvent).instance1Property().get());
                    return new ConditionResult(true, theAffectedInstances);
                }

                if (theSecondaryObject.uuidProperty().get().equals(theO1.uuidProperty().get()) &&
                        thePrimaryObject.uuidProperty().get().equals(theO2.uuidProperty().get())) {
                    // Collision
                    List<GameObjectInstance> theAffectedInstances = new ArrayList<GameObjectInstance>();
                    theAffectedInstances.add(((GameObjectCollision) aEvent).instance2Property().get());
                    return new ConditionResult(true, theAffectedInstances);
                }
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (!primaryObject.isNull()) {
            theResult.put("primaryObjectUuid", primaryObject.get().uuidProperty().get());
        }
        if (!secondaryObject.isNull()) {
            theResult.put("secondaryObjectUuid", secondaryObject.get().uuidProperty().get());
        }
        return theResult;
    }

    public static ObjectCollisionCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        ObjectCollisionCondition theCondition = new ObjectCollisionCondition();
        String thePrimaryObject = (String) aSerializedData.get("primaryObjectUuid");
        if (thePrimaryObject != null) {
            theCondition.primaryObject.setQuietly(aGameScene.findGameObjectByID(thePrimaryObject));
        }
        String theSecondaryObject = (String) aSerializedData.get("secondaryObjectUuid");
        if (theSecondaryObject != null) {
            theCondition.secondaryObject.setQuietly(aGameScene.findGameObjectByID(theSecondaryObject));
        }
        return theCondition;
    }
}