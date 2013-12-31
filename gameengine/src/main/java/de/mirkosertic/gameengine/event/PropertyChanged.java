package de.mirkosertic.gameengine.event;

public class PropertyChanged extends GameEvent {

    public final Property property;
    public final Object oldValue;

    PropertyChanged(Property aProperty, Object aOldValue) {
        super("PropertyChanged");
        property = aProperty;
        oldValue = aOldValue;
    }

    public Object getOwner() {
        return property.getOwner();
    }
}