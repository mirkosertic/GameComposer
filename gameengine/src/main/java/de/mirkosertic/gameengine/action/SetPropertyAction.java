package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetPropertyAction";

    private final Property<GameObjectInstance> instance;
    private final Property<String> propertyName;
    private final Property<Object> propertyValue;

    public SetPropertyAction() {
        instance = new Property<GameObjectInstance>(this, "gameObject", (GameObjectInstance) null);
        propertyName = new Property<String>(this, "propertyName", (String) null);
        propertyValue = new Property<Object>(this, "propertyValue", (Object) null);
    }

    public Property<GameObjectInstance> instanceProperty() {
        return instance;
    }

    public Property<String> propertyNameProperty() {
        return propertyName;
    }

    public Property<Object> propertyValueProperty() {
        return propertyValue;
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

    public static SetPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetPropertyAction theResult = new SetPropertyAction();
        theResult.instance.setQuietly(aGameScene.findGameObjectInstanceByID((String) aSerializedData.get("instance")));
        theResult.propertyName.setQuietly((String) aSerializedData.get("propertyName"));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get("propertyValue");
        theResult.propertyValue.setQuietly(ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, thePropertyValue));
        return theResult;
    }
}