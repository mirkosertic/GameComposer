package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetGameObjectInstancePropertyAction implements Action {

    public static final String TYPE_VALUE = "SetGameObjectInstancePropertyAction";

    private final Property<GameObjectInstance> instance;
    private final Property<SetPropertyCommand> value;

    public SetGameObjectInstancePropertyAction() {
        instance = new Property<GameObjectInstance>(this, "gameObject", (GameObjectInstance) null);
        value = new Property<SetPropertyCommand>(this, "value", (SetPropertyCommand) null);
    }

    @Override
    public void invoke(GameScene aScene) {
        GameObjectInstance theObject = instance.get();
        SetPropertyCommand theCommand = value.get();
        theObject.setPropertyByName(theCommand.propertyName, theCommand.propertyValue);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("instance", instance.get().uuidProperty().get());
        theResult.put("value", value.get().serialize());
        return theResult;
    }

    public static SetGameObjectInstancePropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetGameObjectInstancePropertyAction theResult = new SetGameObjectInstancePropertyAction();
        theResult.instance.setQuietly(aGameScene.findGameObjectInstanceByID((String) aSerializedData.get("instance")));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get("value");
        theResult.value.setQuietly(SetPropertyCommand.deserialize(aGameScene, thePropertyValue));
        return theResult;
    }
}