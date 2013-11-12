package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class MatchEventCondition implements Condition {

    public static final String TYPE_VALUE = "MatchEventCondition";

    private Property<String> eventType;
    private Map<String, String> eventProperties;

    public MatchEventCondition() {
        eventType = new Property<String>(this, "eventType", (String) null);
        eventProperties = new HashMap<String, String>();
    }

    public Property<String> eventTypeProperty() {
        return eventType;
    }

    public void setFilterValue(String aPropertyName, String aPropertyValue) {
        eventProperties.put(aPropertyName, aPropertyValue);
    }

    @Override
    public boolean appliesTo(GameEvent aEvent) {
        String theEventType = aEvent.typeProperty().get();
        if (theEventType.equals(eventType)) {
            for (Map.Entry<String, String> theEventPropertiesKey : eventProperties.entrySet()) {
                String thePropertyname = theEventPropertiesKey.getKey();
                String theExpectedValueAsString = theEventPropertiesKey.getValue();
                ReadOnlyProperty theEventProperty = aEvent.getPropertyByName(thePropertyname);
                if (theEventProperty != null) {
                    String thePropertyValueAsString = null;
                    Object thePropertyValue = theEventProperty.get();
                    if (thePropertyValue instanceof String) {
                        thePropertyValueAsString = (String) thePropertyValue;
                    }
                    if (thePropertyValue instanceof GameObjectInstance) {
                        thePropertyValueAsString = ((GameObjectInstance) thePropertyValue).getOwnerGameObject()
                                .nameProperty().get();
                    }
                    if (thePropertyValue instanceof GameObject) {
                        thePropertyValueAsString = ((GameObject) thePropertyValue).nameProperty().get();
                    }
                    if (thePropertyValue instanceof GameKeyCode) {
                        thePropertyValueAsString = ((GameKeyCode) thePropertyValue).name();
                    }
                    if (thePropertyValue instanceof Boolean) {
                        thePropertyValueAsString = thePropertyValue.toString();
                    }

                    if (!theExpectedValueAsString.equals(thePropertyValueAsString)) {
                        // Condition not met
                        return false;
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

        Map<String, String> theProperties = new HashMap<String, String>();
        for (Map.Entry<String, String> theEventPropertiesKey : eventProperties.entrySet()) {
            theProperties.put(theEventPropertiesKey.getKey(), theEventPropertiesKey.getValue());
        }
        theResult.put("properties", theProperties);
        return theResult;
    }

    public static MatchEventCondition unmarshall(Map<String, Object> aSerializedData) {
        MatchEventCondition theResult = new MatchEventCondition();
        theResult.eventType.setQuietly((String) aSerializedData.get("eventtype"));
        Map<String, String> thePropertiesMap = (Map<String, String>) aSerializedData.get("properties");
        for (Map.Entry<String, String> theEntry : thePropertiesMap.entrySet()) {
            theResult.eventProperties.put(theEntry.getKey(), theEntry.getValue());
        }
        return theResult;
    }
}
