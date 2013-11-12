package de.mirkosertic.gameengine.event;

import java.util.HashMap;
import java.util.Map;

public class GameEvent {

    private Map<String, ReadOnlyProperty> properties;

    private ReadOnlyProperty<String> type;

    protected GameEvent(String aType) {
        properties = new HashMap<String, ReadOnlyProperty>();
        type = new ReadOnlyProperty<String>(this, "type", aType);
    }

    protected final <T> ReadOnlyProperty<T> registerProperty(ReadOnlyProperty<T> aProperty) {
        properties.put(aProperty.getName(), aProperty);
        return aProperty;
    }

    public ReadOnlyProperty getPropertyByName(String aPropertyName) {
        return properties.get(aPropertyName);
    }

    public ReadOnlyProperty<String> typeProperty() {
        return type;
    }
}