package de.mirkosertic.gameengine.event;

import java.util.HashSet;
import java.util.Set;

public class Property<T> {

    private String name;
    private T value;
    private Object owner;
    private Set<GameEventListener<PropertyChangeEvent>> changeListener;

    public Property(Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        changeListener = new HashSet<GameEventListener<PropertyChangeEvent>>();
        owner = aOwner;
        name = aName;
        if (aListener != null) {
            changeListener.add(aListener);
        }
        value = aDefaultValue;
    }

    public Property(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        this(aOwner, aName, null, aListener);
    }

    public Property(Object aOwner, String aName, T aDefaultValue) {
        this(aOwner, aName, aDefaultValue, null);
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

    public void set(T aValue) {
        T theOldValue = value;
        value = aValue;
        Set<GameEventListener> theKnownListener = new HashSet<GameEventListener>(changeListener);
        for (GameEventListener<PropertyChangeEvent> theListener : theKnownListener) {
            theListener.handleGameEvent(new PropertyChangeEvent(this, theOldValue));
        }
    }

    public boolean isNull() {
        return value == null;
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
