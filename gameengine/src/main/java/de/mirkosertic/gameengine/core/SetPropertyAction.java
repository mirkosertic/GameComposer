package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SetPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetPropertyAction";
    public static final String PROPERTY_NAME_PROPERTY = "propertyName";
    public static final String PROPERTY_VALUE_PROPERTY = "propertyValue";

    private final Property<String> propertyName;
    private final Property<Object> propertyValue;

    @UsedByReflection
    public SetPropertyAction() {
        propertyName = new Property<String>(this, PROPERTY_NAME_PROPERTY, (String) null);
        propertyValue = new Property<Object>(this, PROPERTY_VALUE_PROPERTY, (Object) null);
    }

    public Property<String> propertyNameProperty() {
        return propertyName;
    }

    public Property<Object> propertyValueProperty() {
        return propertyValue;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aConditionResult) {
        for (GameObjectInstance theInstance : aConditionResult.getAffectedInstances()) {
            theInstance.setPropertyByName(propertyName.get(), propertyValue.get());
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(PROPERTY_NAME_PROPERTY, propertyName.get());
        theResult.put(PROPERTY_VALUE_PROPERTY, ObjectValueOrReferenceEncoder.encodeValueOrReference(propertyValue.get()));
        return theResult;
    }

    public static SetPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetPropertyAction theResult = new SetPropertyAction();
        theResult.propertyName.setQuietly((String) aSerializedData.get(PROPERTY_NAME_PROPERTY));
        Map<String, Object> thePropertyValue = (Map<String, Object>) aSerializedData.get(PROPERTY_VALUE_PROPERTY);
        theResult.propertyValue.setQuietly(ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, thePropertyValue));
        return theResult;
    }
}