package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.types.Color;

public class ColorProperty extends Property<Color> {

    ColorProperty(Object aOwner, String aName, GameEventListener<PropertyChangeEvent> aListener) {
        super(aOwner, aName, aListener);
    }

    @Override
    Color createInitialValue() {
        return new Color(0, 0, 0);
    }
}
