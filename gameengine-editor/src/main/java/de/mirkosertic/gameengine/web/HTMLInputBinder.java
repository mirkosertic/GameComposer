package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;

public class HTMLInputBinder {

    public interface Converter<T, V> {

        V convertFrom(T aValue);

        T convertTo(V aValue);
    }

    private final GameEventListener<PropertyChanged> listener;
    private final Property property;

    HTMLInputBinder(GameEventListener<PropertyChanged> aListener, Property aProperty) {
        listener = aListener;
        property = aProperty;
    }

    public void unbind() {
        property.removeChangeListener(listener);
    }
}