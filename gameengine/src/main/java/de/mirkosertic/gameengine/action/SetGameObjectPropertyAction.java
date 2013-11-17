package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetGameObjectPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetGameObjectPropertyAction";

    private final Property<GameObject> gameObject;
    private final Property<SetPropertyCommand> value;

    public SetGameObjectPropertyAction() {
        gameObject = new Property<GameObject>(this, "gameObject", (GameObject) null);
        value = new Property<SetPropertyCommand>(this, "value", (SetPropertyCommand) null);
    }

    @Override
    public void invoke(GameScene aScene) {
        GameObject theObject = gameObject.get();
        SetPropertyCommand theCommand = value.get();
        theObject.setPropertyByName(theCommand.propertyName, theCommand.propertyValue);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("gameObject", gameObject.get().uuidProperty().get());
        theResult.put("value", value.get().serialize());
        return theResult;
    }

    public static SetGameObjectPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetGameObjectPropertyAction theResult = new SetGameObjectPropertyAction();
        theResult.gameObject.setQuietly(aGameScene.findGameObjectByID((String) aSerializedData.get("gameObject")));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get("value");
        theResult.value.setQuietly(SetPropertyCommand.deserialize(aGameScene, thePropertyValue));
        return theResult;
    }
}