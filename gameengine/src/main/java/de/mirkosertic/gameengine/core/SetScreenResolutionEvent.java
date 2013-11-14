package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.types.Size;

public class SetScreenResolutionEvent extends GameEvent {

    private final ReadOnlyProperty<Size> screenSize;

    public SetScreenResolutionEvent(Size aSize) {
        super("SetScreenResolutionEvent");
        screenSize = registerProperty(new ReadOnlyProperty<Size>(this, "screenSize", aSize));
    }

    public ReadOnlyProperty<Size> screenSizeProperty() {
        return screenSize;
    }
}
