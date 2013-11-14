package de.mirkosertic.gameengine.event;

public class ReadOnlyProperty<T> {

    private final String name;
    T value;
    private final Object owner;

    public ReadOnlyProperty(Object aOwner, String aName, T aDefaultValue) {
        owner = aOwner;
        name = aName;
        value = aDefaultValue;
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
