package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.ValueProvider;

public class ReadOnlyProperty<T> implements ValueProvider {

    private final String name;

    T value;
    private final Object owner;
    final Class<T> type;

    public ReadOnlyProperty(Class<T> aType, Object aOwner, String aName, T aDefaultValue) {
        type = aType;
        owner = aOwner;
        name = aName;
        value = aDefaultValue;
    }

    public Class<T> getType() {
        return type;
    }

    public Object getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public T get() {
        return value;
    }

    public boolean isNull() {
        return value == null;
    }
}
