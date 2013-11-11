package de.mirkosertic.gameengine.event;

public class PropertyChangeEvent extends GameEvent {

    private Property property;
    private Object oldValue;

    PropertyChangeEvent(Property aProperty, Object aOldValue) {
        property = aProperty;
        oldValue = aOldValue;
    }

    public Object getOwner() {
        return property.getOwner();
    }

    public Object getNewValue() {
        return property.get();
    }
}
