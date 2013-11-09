package de.mirkosertic.gameengine.event;

public class PropertyChangeEvent extends GameEvent {

    private Property property;
    private Object oldValue;

    PropertyChangeEvent(Property aProperty, Object aOldValue) {
        property = aProperty;
        oldValue = aOldValue;
    }
}
