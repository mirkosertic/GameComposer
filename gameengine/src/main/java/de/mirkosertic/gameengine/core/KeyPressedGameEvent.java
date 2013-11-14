package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class KeyPressedGameEvent extends GameEvent {

    private final ReadOnlyProperty<GameKeyCode> keyCode;

    public KeyPressedGameEvent(GameKeyCode aKeyCode) {
        super("KeyPressedGameEvent");
        keyCode = registerProperty(new ReadOnlyProperty<GameKeyCode>(this, "keyCode", aKeyCode));
    }

    public ReadOnlyProperty<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }
}
