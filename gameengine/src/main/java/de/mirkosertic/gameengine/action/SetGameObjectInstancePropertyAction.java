package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetGameObjectInstancePropertyAction implements Action {

    public static final String TYPE_VALUE = "SetGameObjectInstancePropertyAction";

    private final Property<GameObjectInstance> instance;
    private final Property<String> propertyName;
    private final Property<Object> propertyValue;

    public SetGameObjectInstancePropertyAction() {
        instance = new Property<GameObjectInstance>(this, "gameObject", (GameObjectInstance) null);
        propertyName = new Property<String>(this, "propertyName", (String) null);
        propertyValue = new Property<Object>(this, "propertyValue", (Object) null);
    }

    @Override
    public void invoke(GameScene aScene) {
        GameObjectInstance theObject = instance.get();
        theObject.setPropertyByName(propertyName.get(), propertyValue.get());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("instance", instance.get().uuidProperty().get());
        theResult.put("propertyName", propertyName.get());
        theResult.put("propertyValue", ObjectValueOrReferenceEncoder.encodeValueOrReference(propertyValue.get()));
        return theResult;
    }

    public static SetGameObjectInstancePropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetGameObjectInstancePropertyAction theResult = new SetGameObjectInstancePropertyAction();
        theResult.instance.setQuietly(aGameScene.findGameObjectInstanceByID((String) aSerializedData.get("instance")));
        theResult.propertyName.setQuietly((String) aSerializedData.get("propertyName"));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get("propertyValue");
        theResult.propertyValue.setQuietly(ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, thePropertyValue));
        return theResult;
    }
}