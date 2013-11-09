package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.types.Angle;

public class AngleProperty extends Property<Angle> {

    AngleProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Angle createInitialValue() {
        return new Angle(0);
    }
}
