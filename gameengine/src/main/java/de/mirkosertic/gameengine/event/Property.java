package de.mirkosertic.gameengine.event;

public abstract class Property<T> {

    private String name;
    private T value;
    private Object owner;
    private GameEventListener<PropertyChangeEvent> changeListener;

    Property(Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        owner = aOwner;
        name = aName;
        changeListener = aListener;
        value = aDefaultValue;
    }


    Property(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        this(aOwner, aName, null, aListener);
    }

    abstract T createInitialValue();

    public Object getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public T get() {
        return value;
    }

    public void set(T aValue) {
        T theOldValue = value;
        value = aValue;
        changeListener.handleGameEvent(new PropertyChangeEvent(this, theOldValue));
    }

    public void setQuietly(T aValue) {
        value = aValue;
    }
}