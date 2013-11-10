package de.mirkosertic.gameengine.event;

import java.util.HashSet;
import java.util.Set;

public abstract class Property<T> {

    private String name;
    private T value;
    private Object owner;
    private Set<GameEventListener<PropertyChangeEvent>> changeListener;

    Property(Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        changeListener = new HashSet<GameEventListener<PropertyChangeEvent>>();
        owner = aOwner;
        name = aName;
        changeListener.add(aListener);
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
        for (GameEventListener<PropertyChangeEvent> theListener : changeListener) {
            theListener.handleGameEvent(new PropertyChangeEvent(this, theOldValue));
        }
    }

    public void setQuietly(T aValue) {
        value = aValue;
    }

    public void addChangeListener(GameEventListener<PropertyChangeEvent> aListener) {
        changeListener.add(aListener);
    }

    public void removeChangeListener(GameEventListener<PropertyChangeEvent> aListener) {
        changeListener.remove(aListener);
    }
}