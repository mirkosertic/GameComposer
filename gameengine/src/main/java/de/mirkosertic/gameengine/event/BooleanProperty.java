package de.mirkosertic.gameengine.event;

public class BooleanProperty extends Property<Boolean> {

    BooleanProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Boolean createInitialValue() {
        return Boolean.FALSE;
    }
}
