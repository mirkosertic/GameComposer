package de.mirkosertic.gameengine.event;

public class StringProperty extends Property<String> {

    StringProperty(Object aOwner, String aName, String aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aDefaultValue, aListener);
    }

    StringProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    String createInitialValue() {
        return null;
    }
}
