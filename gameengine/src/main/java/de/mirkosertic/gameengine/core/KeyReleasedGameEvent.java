package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class KeyReleasedGameEvent extends GameEvent {

    private ReadOnlyProperty<GameKeyCode> keyCode;

    public KeyReleasedGameEvent(GameKeyCode aKeyCode) {
        super("KeyReleasedGameEvent");
        keyCode = registerProperty(new ReadOnlyProperty<GameKeyCode>(this, "keyCode", aKeyCode));
    }

    public ReadOnlyProperty<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }
}
