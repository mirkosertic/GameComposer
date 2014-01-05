package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.ValueProvider;

public class PropertyValueProvider implements ValueProvider {

    private final Property property;

    public PropertyValueProvider(Property aProperty) {
        property = aProperty;
    }

    @Override
    public Object get() {
        return property.get();
    }
}
