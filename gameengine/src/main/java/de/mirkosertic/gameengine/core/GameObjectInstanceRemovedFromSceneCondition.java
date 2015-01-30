package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class GameObjectInstanceRemovedFromSceneCondition implements Condition {

    static final String TYPE_VALUE = "GameObjectInstanceRemovedFromSceneCondition";

    public static final String GAME_OBJECT_PROPERTY = "gameObject";

    private final Property<GameObject> gameObject;

    @UsedByReflection
    public GameObjectInstanceRemovedFromSceneCondition() {
        gameObject = new Property<>(GameObject.class, this, GAME_OBJECT_PROPERTY, (GameObject) null);
    }

    public Property<GameObject> gameObjectProperty() {
        return gameObject;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof GameObjectInstanceRemovedFromScene) {
            GameObjectInstanceRemovedFromScene theEvent = (GameObjectInstanceRemovedFromScene) aEvent;
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

    public static GameObjectInstanceRemovedFromSceneCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        GameObjectInstanceRemovedFromSceneCondition theResult = new GameObjectInstanceRemovedFromSceneCondition();
        String theObjectUUID = (String) aSerializedData.get("objectUUID");
        if (theObjectUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findObjectByID(theObjectUUID));
        }
        return theResult;
    }
}
