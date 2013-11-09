package de.mirkosertic.gameengine.event;

public class IntegerProperty extends Property<Integer> {

    IntegerProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Integer createInitialValue() {
        return null;
    }
}
