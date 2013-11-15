package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.types.ResourceName;

public class MatchEventCondition implements Condition {

    public static final String TYPE_VALUE = "MatchEventCondition";

    private final Property<String> eventType;
    private final Map<String, Object> eventProperties;

    public MatchEventCondition() {
        eventType = new Property<String>(this, "eventType", (String) null);
        eventProperties = new HashMap<String, Object>();
    }

    public Property<String> eventTypeProperty() {
        return eventType;
    }

    public void setFilterValue(String aPropertyName, Object aPropertyValue) {
        eventProperties.put(aPropertyName, aPropertyValue);
    }

    public Object getFilterValue(String aPropertyName) {
        return eventProperties.get(aPropertyName);
    }

    @Override
    public boolean appliesTo(GameEvent aEvent) {
        String theEventType = aEvent.typeProperty().get();
        if (theEventType.equals(eventType.get())) {
            for (Map.Entry<String, Object> theEventPropertiesKey : eventProperties.entrySet()) {
                String thePropertyname = theEventPropertiesKey.getKey();
                Object theObjectValue = theEventPropertiesKey.getValue();
                ReadOnlyProperty theEventProperty = aEvent.getPropertyByName(thePropertyname);
                if (theEventProperty != null) {
                    Object thePropertyValue = theEventProperty.get();
                    if (thePropertyValue instanceof String) {
                        if (!thePropertyValue.equals(theObjectValue)) {
                            return false;
                        }
                    }
                    if (thePropertyValue instanceof ResourceName) {
                        if (!((ResourceName) thePropertyValue).name.equals(((ResourceName) theObjectValue).name)) {
                            return false;
                        }
                    }
                    if (thePropertyValue instanceof GameKeyCode) {
                        if (!((GameKeyCode) thePropertyValue).name().equals(((GameKeyCode) theObjectValue).name())) {
                            return false;
                        }
                    }
                    if (thePropertyValue instanceof GameObjectInstance) {
                        GameObjectInstance theInstance = (GameObjectInstance) thePropertyValue;
                        if (theObjectValue instanceof GameObjectInstance) {
                            GameObjectInstance theOtherInstance = (GameObjectInstance) theObjectValue;
                            if (!theInstance.uuidProperty().get()
                                    .equals(theOtherInstance.uuidProperty().get())) {
                                return false;
                            }
                        } else {
                            GameObject theOtherObject = (GameObject) theObjectValue;
                            if (!theInstance.getOwnerGameObject().uuidProperty().get()
                                    .equals(theOtherObject.uuidProperty().get())) {
                                return false;
                            }
                        }
                    }
                    if (thePropertyValue instanceof GameObject) {
                        GameObject theObject = (GameObject) thePropertyValue;
                        if (theObjectValue instanceof GameObject) {
                            GameObject theOtherValue = (GameObject) theObjectValue;
                            if (!theObject.uuidProperty().get()
                                    .equals(theOtherValue.uuidProperty().get())) {
                                return false;
                            }
                        } else {
                            GameObjectInstance theOtherInstance = (GameObjectInstance) theObjectValue;
                            if (!theObject.uuidProperty().get()
                                    .equals(theOtherInstance.getOwnerGameObject().uuidProperty().get())) {
                                return false;
                            }
                        }
                    }
                    if (thePropertyValue instanceof Boolean) {
                        if (!(Boolean) thePropertyValue == (Boolean) theObjectValue) {
                            return false;
                        }
                    }
                } else {
                    // Property not found, so condition not met
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("eventtype", eventType.get());

        Map<String, Object> theProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> theEventPropertiesKey : eventProperties.entrySet()) {
            theProperties.put(theEventPropertiesKey.getKey(),
                    ObjectValueOrReferenceEncoder.encodeValueOrReference(theEventPropertiesKey.getValue()));
        }
        theResult.put("properties", theProperties);
        return theResult;
    }

    public static MatchEventCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        MatchEventCondition theResult = new MatchEventCondition();
        theResult.eventType.setQuietly((String) aSerializedData.get("eventtype"));
        Map<String, Object> thePropertiesMap = (Map<String, Object>) aSerializedData.get("properties");
        for (Map.Entry<String, Object> theEntry : thePropertiesMap.entrySet()) {
            Map<String, Object> theValue = (Map<String, Object>) theEntry.getValue();
            theResult.eventProperties.put(theEntry.getKey(), ObjectValueOrReferenceEncoder.decodeValueOrReference(aGameScene, theValue));
        }
        return theResult;
    }
}
