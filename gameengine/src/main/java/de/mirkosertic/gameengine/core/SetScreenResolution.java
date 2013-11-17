package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.types.Size;

public class SetScreenResolution extends GameEvent {

    private final Property<Size> screenSize;

    public SetScreenResolution(Size aSize) {
        super("SetScreenResolution");
        screenSize = registerProperty(new Property<Size>(this, "screenSize", aSize));
    }

    public ReadOnlyProperty<Size> screenSizeProperty() {
        return screenSize;
    }
}