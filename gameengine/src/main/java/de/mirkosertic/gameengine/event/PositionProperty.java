package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.types.Position;

public class PositionProperty extends Property<Position> {

    PositionProperty(Object aOwner, String aName, Position aDefaultValue, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aDefaultValue, aListener);
    }

    PositionProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Position createInitialValue() {
        return new Position(0, 0);
    }
}
