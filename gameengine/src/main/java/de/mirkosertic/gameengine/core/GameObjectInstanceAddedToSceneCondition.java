package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class GameObjectInstanceAddedToSceneCondition implements Condition {

    static final String TYPE_VALUE = "GameObjectInstanceAddedToSceneCondition";

    private final Property<GameObject> gameObject;

    @UsedByReflection
    public GameObjectInstanceAddedToSceneCondition() {
        gameObject = new Property<GameObject>(this, "gameObject", (GameObject) null);
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
                return new ConditionResult(true, new GameObjectInstance[] {theInstance});
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
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
            theResult.gameObject.setQuietly(aGameScene.findGameObjectByID(theObjectUUID));
        }
        return theResult;
    }
}
