package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class KeyReleased extends GameEvent {

    private final Property<GameKeyCode> keyCode;

    KeyReleased(GameKeyCode aKeyCode) {
        super("KeyReleased");
        keyCode = registerProperty(new Property<GameKeyCode>(this, "keyCode", aKeyCode));
    }

    public ReadOnlyProperty<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }
}
