package de.mirkosertic.gameengine.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Property<T> extends ReadOnlyProperty<T> {

    private final Set<GameEventListener<PropertyChanged>> changeListener;

    public Property(Class<T> aType, Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChanged> aListener) {
        super(aType, aOwner, aName, aDefaultValue);
        changeListener = new HashSet<>();
        if (aListener != null) {
            changeListener.add(aListener);
        }
    }

    public Property(Class<T> aType, Object aOwner, String aName, GameEventListener<PropertyChanged> aListener) {
        this(aType, aOwner, aName, null, aListener);
    }

    public Property(Class<T> aType, Object aOwner, String aName, T aDefaultValue) {
        this(aType, aOwner, aName, aDefaultValue, null);
    }

    public void set(T aValue) {

        if (aValue == value) {
            // Nothing to do
            return;
        } else if (value != null) {
            if (value.equals(aValue)) {
                // Nothing to do
                return;
            }
        } else if (aValue != null) {
            if (aValue.equals(value)) {
                // Nothing to do
                return;
            }
        } else if (value == aValue) {
            // Nothing to do
            return;
        }

        if (value == null && aValue == null) {
            // Nothing to do
            return;
        }

        T theOldValue = value;
        value = aValue;

        if (!changeListener.isEmpty()) {
            PropertyChanged theEvent = new PropertyChanged(this, theOldValue);

            Set<GameEventListener> theKnownListener = new HashSet<GameEventListener>(changeListener);
            for (GameEventListener<PropertyChanged> theListener : theKnownListener) {
                theListener.handleGameEvent(theEvent);
            }
        }
    }

    public void setQuietly(T aValue) {
        value = aValue;
    }

    public Set<GameEventListener<PropertyChanged>> getChangeListener() {
        return Collections.unmodifiableSet(changeListener);
    }

    public void addChangeListener(GameEventListener<PropertyChanged> aListener) {
        changeListener.add(aListener);
    }

    public void removeChangeListener(GameEventListener<PropertyChanged> aListener) {
        changeListener.remove(aListener);
    }
}