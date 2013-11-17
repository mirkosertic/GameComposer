package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class KeyPressedGame extends GameEvent {

    private final Property<GameKeyCode> keyCode;

    public KeyPressedGame(GameKeyCode aKeyCode) {
        super("KeyPressedGame");
        keyCode = registerProperty(new Property<GameKeyCode>(this, "keyCode", aKeyCode));
    }

    public ReadOnlyProperty<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }
}
