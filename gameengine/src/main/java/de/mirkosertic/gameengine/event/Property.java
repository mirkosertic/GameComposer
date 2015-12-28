package de.mirkosertic.gameengine.event;

import java.util.*;

public class Property<T> extends ReadOnlyProperty<T> {

    private GameEventListener<PropertyChanged> changeListener[];

    private long lastChanged;

    public Property(Class<T> aType, Object aOwner, String aName, T aDefaultValue, GameEventListener<PropertyChanged> aListener) {
        super(aType, aOwner, aName, aDefaultValue);
        if (aListener != null) {
            changeListener = new GameEventListener[] {aListener};
        } else {
            changeListener = new GameEventListener[0];
        }
        lastChanged = System.currentTimeMillis();
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
        }
        if (value != null) {
            if (value.equals(aValue)) {
                // Nothing to do
                return;
            }
        }
        if (aValue != null) {
            if (aValue.equals(value)) {
                // Nothing to do
                return;
            }
        }

        if (value == null && aValue == null) {
            // Nothing to do
            return;
        }

        T theOldValue = value;
        value = aValue;

        lastChanged = System.currentTimeMillis();

        if (changeListener.length > 0) {
            PropertyChanged theEvent = new PropertyChanged(this, theOldValue);

            for (GameEventListener<PropertyChanged> theListener : changeListener) {
                theListener.handleGameEvent(theEvent);
            }
        }
    }

    public void setQuietly(T aValue) {
        value = aValue;
    }

    public long getLastChanged() {
        return lastChanged;
    }

    public List<GameEventListener<PropertyChanged>> getChangeListener() {
        List<GameEventListener<PropertyChanged>> theNewListener = new ArrayList<>();
        for (GameEventListener<PropertyChanged> theListener : changeListener) {
            theNewListener.add(theListener);
        }
        return theNewListener;
    }

    public void addChangeListener(GameEventListener<PropertyChanged> aListener) {
        List<GameEventListener<PropertyChanged>> theNewListener = new ArrayList<>();
        for (GameEventListener<PropertyChanged> theListener : changeListener) {
            theNewListener.add(theListener);
        }
        theNewListener.add(aListener);
        changeListener = theNewListener.toArray(new GameEventListener[theNewListener.size()]);
    }

    public void removeChangeListener(GameEventListener<PropertyChanged> aListener) {
        List<GameEventListener<PropertyChanged>> theNewListener = new ArrayList<>();
        for (GameEventListener<PropertyChanged> theListener : changeListener) {
            theNewListener.add(theListener);
        }
        theNewListener.remove(aListener);
        changeListener = theNewListener.toArray(new GameEventListener[theNewListener.size()]);
    }
}