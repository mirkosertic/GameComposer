package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.types.ResourceName;

public class MatchEventCondition implements Condition {

    public static final String TYPE_VALUE = "MatchEventCondition";

    private Property<String> eventType;
    private Map<String, Object> eventProperties;

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
        if (theEventType.equals(eventType)) {
            for (Map.Entry<String, Object> theEventPropertiesKey : eventProperties.entrySet()) {
                String thePropertyname = theEventPropertiesKey.getKey();
                Object theObjectValue = theEventPropertiesKey.getValue();
                ReadOnlyProperty theEventProperty = aEvent.getPropertyByName(thePropertyname);
                if (theEventProperty != null) {
                    Object thePropertyValue = theEventProperty.get();
                    if (thePropertyValue instanceof String) {
                        return thePropertyValue.equals(theObjectValue);
                    }
                    if (thePropertyValue instanceof ResourceName) {
                        return ((ResourceName) thePropertyValue).name.equals(((ResourceName) theObjectValue).name);
                    }
                    if (thePropertyValue instanceof GameKeyCode) {
                        return ((GameKeyCode) thePropertyValue).name().equals(((GameKeyCode) theObjectValue).name());
                    }
                    if (thePropertyValue instanceof GameObjectInstance) {
                        return ((GameObjectInstance) thePropertyValue).uuidProperty().get()
                                .equals(((GameObjectInstance) theObjectValue).uuidProperty().get());
                    }
                    if (thePropertyValue instanceof GameObject) {
                        return ((GameObject) thePropertyValue).uuidProperty().get()
                                .equals(((GameObject) theObjectValue).uuidProperty().get());
                    }
                    if (thePropertyValue instanceof Boolean) {
                        return ((Boolean) thePropertyValue).booleanValue() == ((Boolean) theObjectValue).booleanValue();
                    }
                }
                // Property not found, so condition not met
                return false;
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
