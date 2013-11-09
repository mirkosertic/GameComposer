package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.types.Size;

public class SizeProperty extends Property<Size> {

    SizeProperty(Object aOwner, String aName, Size aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aDefaultValue, aListener);
    }

    SizeProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Size createInitialValue() {
        return new Size(0, 0);
    }
}