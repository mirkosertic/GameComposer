package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Position;

import java.util.HashMap;
import java.util.Map;

public class SpawnGameObjectInstanceAction implements Action {

    public static final String TYPE_VALUE = "SpawnGameObjectInstanceAction";

    private final Property<GameObject> referenceObject;
    private final Property<GameObject> gameObject;
    private final Property<Position> offset;

    public SpawnGameObjectInstanceAction() {
        gameObject = new Property<GameObject>(this, "gameObject", (GameObject) null);
        referenceObject = new Property<GameObject>(this, "referenceObject", (GameObject) null);
        offset = new Property<Position>(this, "offset", new Position(0, 0));
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
                    aScene.addGameObjectInstance(theNewInstance);
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        GameObject theObject = gameObject.get();
        if (theObject != null) {
            theResult.put("objectUuid", theObject.uuidProperty().get());
        }
        GameObject theReferenceObject = gameObject.get();
        if (theReferenceObject != null) {
            theResult.put("referenceObjectUuid", theReferenceObject.uuidProperty().get());
        }
        theResult.put("offset", offset.get().serialize());
        return theResult;
    }

    public static SpawnGameObjectInstanceAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SpawnGameObjectInstanceAction theResult = new SpawnGameObjectInstanceAction();
        String theObjectUUID = (String) aSerializedData.get("objectUuid");
        if (theObjectUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findGameObjectByID(theObjectUUID));
        }
        String theOReferemceObjectUUID = (String) aSerializedData.get("referenceObjectUuid");
        if (theOReferemceObjectUUID != null) {
            theResult.referenceObject.setQuietly(aGameScene.findGameObjectByID(theOReferemceObjectUUID));
        }

        Map<String, Object> thePosition = (Map<String, Object>) aSerializedData.get("offset");
        if (thePosition != null) {
            theResult.offset.setQuietly(Position.deserialize(thePosition));
        }
        return theResult;
    }
}