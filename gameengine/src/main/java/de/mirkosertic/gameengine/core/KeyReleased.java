package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class KeyReleased extends GameEvent {

    private final Property<GameKeyCode> keyCode;

    public KeyReleased(GameKeyCode aKeyCode) {
        super("KeyReleased");
        keyCode = registerProperty(new Property<GameKeyCode>(this, "keyCode", aKeyCode));
    }

    public ReadOnlyProperty<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }
}
