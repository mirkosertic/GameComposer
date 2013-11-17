package de.mirkosertic.gameengine.event;

public class PropertyChanged extends GameEvent {

    private final Property<Property> property;
    private final Property<Object> oldValue;

    PropertyChanged(Property aProperty, Object aOldValue) {
        super("PropertyChanged");
        property = registerProperty(new Property<Property>(this, "property", aProperty));
        oldValue = registerProperty(new Property<Object>(this, "oldValue", aOldValue));
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