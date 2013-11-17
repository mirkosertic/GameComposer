package de.mirkosertic.gameengine.event;

import java.util.HashMap;
import java.util.Map;

public class PropertyAware {

    private final Map<String, Property> properties;

    protected PropertyAware() {
        properties = new HashMap<String, Property>();
    }

    protected final <T> Property<T> registerProperty(Property<T> aProperty) {
        properties.put(aProperty.getName(), aProperty);
        return aProperty;
    }

    public ReadOnlyProperty getPropertyByName(String aPropertyName) {
        return properties.get(aPropertyName);
    }

    public boolean setPropertyByName(String aPropertyName, Object aPropertyValue) {
        Property theProperty = properties.get(aPropertyName);
        if (theProperty == null) {
            return false;
        }

        theProperty.set(aPropertyValue);
        return true;
    }
}