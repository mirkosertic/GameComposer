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
        gameObject = new Property<GameObject>(this, GAME_OBJECT_PROPERTY, (GameObject) null);
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
                            return new ConditionResult(true, new GameObjectInstance[] {theInstance});
                        }
                    }
                }
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
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
            theResult.gameObject.setQuietly(aGameScene.findGameObjectByID(theGameObjectUUID));
        }
        return theResult;
    }
}
