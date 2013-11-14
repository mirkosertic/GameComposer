package de.mirkosertic.gameengine.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Property<T> extends ReadOnlyProperty<T> {

    private final Set<GameEventListener<PropertyChangeEvent>> changeListener;

    public Property(Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aDefaultValue);
        changeListener = new HashSet<GameEventListener<PropertyChangeEvent>>();
        if (aListener != null) {
            changeListener.add(aListener);
        }
    }

    public Property(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        this(aOwner, aName, null, aListener);
    }

    public Property(Object aOwner, String aName, T aDefaultValue) {
        this(aOwner, aName, aDefaultValue, null);
    }

    public void set(T aValue) {
        T theOldValue = value;
        value = aValue;
        Set<GameEventListener> theKnownListener = new HashSet<GameEventListener>(changeListener);
        for (GameEventListener<PropertyChangeEvent> theListener : theKnownListener) {
            theListener.handleGameEvent(new PropertyChangeEvent(this, theOldValue));
        }
    }

    public void setQuietly(T aValue) {
        value = aValue;
    }

    public Set<GameEventListener<PropertyChangeEvent>> getChangeListener() {
        return Collections.unmodifiableSet(changeListener);
    }

    public void addChangeListener(GameEventListener<PropertyChangeEvent> aListener) {
        changeListener.add(aListener);
    }

    public void removeChangeListener(GameEventListener<PropertyChangeEvent> aListener) {
        changeListener.remove(aListener);
    }
}
