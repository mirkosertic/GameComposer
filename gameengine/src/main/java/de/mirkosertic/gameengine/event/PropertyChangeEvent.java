package de.mirkosertic.gameengine.event;

public class PropertyChangeEvent extends GameEvent {

    private final ReadOnlyProperty<Property> property;
    private final ReadOnlyProperty<Object> oldValue;

    PropertyChangeEvent(Property aProperty, Object aOldValue) {
        super("PropertyChangeEvent");
        property = registerProperty(new ReadOnlyProperty<Property>(this, "property", aProperty));
        oldValue = registerProperty(new ReadOnlyProperty<Object>(this, "oldValue", aOldValue));
    }

    public Object getOwner() {
        return property.get().getOwner();
    }

    public ReadOnlyProperty<Property> propertyProperty() {
        return property;
    }

    public ReadOnlyProperty<Object> oldValueProperty() {
        return oldValue;
    }
}